package com.example.classmanager.Service.login;

import com.example.classmanager.Entity.request.AuthenticationRequest;
import com.example.classmanager.Entity.request.IntrospectRequest;
import com.example.classmanager.Entity.request.LogoutRequest;
import com.example.classmanager.Entity.request.RefreshRequest;
import com.example.classmanager.Entity.response.AuthenticationResponse;
import com.example.classmanager.Entity.response.IntrospectResponse;
import com.example.classmanager.Model.login.InvalidatedToken;
import com.example.classmanager.Model.login.User;
import com.example.classmanager.Repository.login.InvalidatedTokenRepository;
import com.example.classmanager.Repository.login.UserRepository;
import com.example.classmanager.exception.AppException;
import com.example.classmanager.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService implements IAuthenticationService{

    InvalidatedTokenRepository invalidatedTokenRepository;

    UserRepository userRepository;
    @NonFinal
    @Value("${signer.key}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${valid.duration}")
    private Long VALID_DURATION;

    @NonFinal
    @Value("${refreshable.duration}")
    private Long REFRESHABLE_DURATION = 3700L;
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_DOES_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenicated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        try {
            verifyToken(token, false);
        }catch (AppException e) {
            return IntrospectResponse.builder()
                    .valid(false)
                    .build();
        }

        return IntrospectResponse.builder()
                .valid(true)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        }catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        // Kiểm tra hiệu lực của token
        var signJWT = verifyToken(request.getToken(), true);

        String jit = signJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        String username = signJWT.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenicated(true)
                .build();
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime(); //Kiểm tra token hết hạn chưa

        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date())))  // Nếu token ko đúng hoặc hết hạn
        {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    // Tạo token bằng username
    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512); //Tạo header

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("devteria.com")
                .issueTime(new Date()) //thời gian hiện tại
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli() //token hết hạn sau 1 tiếng
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); //Tạo payload

        JWSObject jwsObject = new JWSObject(jwsHeader, payload); // Tạo signature

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY)); // Ký token
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" "); // các scope trong oauth2 quy định phân cách nhau bằng dấu cách
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }

}
