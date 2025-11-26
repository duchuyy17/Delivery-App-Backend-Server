package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.service.JwtService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final HttpServletRequest request;
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    private Long REFRESH_DURATION;

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

    @Override
    public String generateToken(Object authData) {
        if(authData instanceof RiderDocument) {
            RiderDocument user = (RiderDocument) authData;
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("deliveryApp.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(
                            Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                    .jwtID(UUID.randomUUID().toString())
                    .claim(
                            "refreshToken",
                            new Date(Instant.now()
                                    .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                                    .toEpochMilli()))
                    .claim("scope", "ROLE_" + "RIDER")
                    .claim("userId", user.getId())
                    .claim("email",user.getUsername())
                    .build();
            Payload payload = new Payload(jwtClaimsSet.toJSONObject());
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            try {
                jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
                return jwsObject.serialize();
            } catch (JOSEException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
}
