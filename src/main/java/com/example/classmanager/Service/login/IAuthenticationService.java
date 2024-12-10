package com.example.classmanager.Service.login;


import com.example.classmanager.Entity.request.AuthenticationRequest;
import com.example.classmanager.Entity.request.IntrospectRequest;
import com.example.classmanager.Entity.request.LogoutRequest;
import com.example.classmanager.Entity.request.RefreshRequest;
import com.example.classmanager.Entity.response.AuthenticationResponse;
import com.example.classmanager.Entity.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
