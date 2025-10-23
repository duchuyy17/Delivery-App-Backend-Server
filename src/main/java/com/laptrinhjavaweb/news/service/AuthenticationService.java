package com.laptrinhjavaweb.news.service;

import java.text.ParseException;

import com.laptrinhjavaweb.news.dto.request.IntrospectRequest;
import com.laptrinhjavaweb.news.dto.request.LogoutRequest;
import com.laptrinhjavaweb.news.dto.request.RefreshTokenRequest;
import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface  AuthenticationService<T> {
    T login(String username, String password);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException;
}
