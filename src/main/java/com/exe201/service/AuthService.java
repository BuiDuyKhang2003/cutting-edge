package com.exe201.service;

import com.exe201.dto.AuthenticationRequest;
import com.exe201.dto.AuthenticationResponse;
import com.exe201.dto.GoogleAuthenticationRequest;
import com.exe201.dto.RegistrationRequest;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface AuthService {
    void register(RegistrationRequest request) throws MessagingException, UnsupportedEncodingException;

    AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;

    void activateAccount(String token, HttpServletResponse response) throws MessagingException, UnsupportedEncodingException;

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, JOSEException;

    AuthenticationResponse findOrCreateUser(GoogleAuthenticationRequest request) throws JOSEException;
}
