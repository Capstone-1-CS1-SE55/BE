package com.example.classmanager.Controller.login;

import com.example.classmanager.Entity.request.AuthenticationRequest;
import com.example.classmanager.Entity.request.IntrospectRequest;
import com.example.classmanager.Entity.request.LogoutRequest;
import com.example.classmanager.Entity.request.RefreshRequest;
import com.example.classmanager.Entity.response.ApiResponse;
import com.example.classmanager.Entity.response.AuthenticationResponse;
import com.example.classmanager.Entity.response.IntrospectResponse;
import com.example.classmanager.Service.login.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse results = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(results)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        IntrospectResponse results = authenticationService.introspect(introspectRequest);
        return ApiResponse.<IntrospectResponse>builder()
                .result(results)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        AuthenticationResponse results = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(results)
                .build();
    }
}
