package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.service.JwtService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final HttpServletRequest request;


    @Override
    public String getCurrentUserId() throws ParseException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid token");
        }
        String token = authHeader.substring(7);
        SignedJWT signedJWT = SignedJWT.parse(token);
        return String.valueOf(signedJWT.getJWTClaimsSet().getClaim("userId"));
    }
}
