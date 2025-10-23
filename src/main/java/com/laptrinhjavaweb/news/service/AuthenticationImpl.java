package com.laptrinhjavaweb.news.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.laptrinhjavaweb.news.mapper.mongo.UserMapperV1;
import com.laptrinhjavaweb.news.mongo.RoleDocument;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.IntrospectRequest;
import com.laptrinhjavaweb.news.dto.request.LogoutRequest;
import com.laptrinhjavaweb.news.dto.request.RefreshTokenRequest;
import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.dto.response.IntrospectResponse;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.entity.InvalidToken;
import com.laptrinhjavaweb.news.entity.RoleEntity;
import com.laptrinhjavaweb.news.entity.UserEntity;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.UserMapper;
import com.laptrinhjavaweb.news.repository.InvalidTokenRepository;
import com.laptrinhjavaweb.news.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService {
    private final UserV1Repository userRepository;
    private final UserMapperV1 userMapper;
    private final InvalidTokenRepository invalidTokenRepository;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    private Long REFRESH_DURATION;

    @Override
    public AuthenticationResponse login(String username, String password) {
        log.info("signKey: {}", SIGNER_KEY);
        UserDocument user =
                userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(password, user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHETICATED);
        }
        UserResponse userResponse = userMapper.toUserResponse(user);
        var token = generateToken(userResponse);
        Set<String> roles = user.getRoles().stream().map(RoleDocument::getCode).collect(Collectors.toSet());
        return AuthenticationResponse.builder()
                .isSuccessful(true)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .token(token)
                .phone(user.getPhone())
                .roles(roles)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            InvalidToken invalidToken = InvalidToken.builder()
                    .id(signedJWT.getJWTClaimsSet().getJWTID())
                    .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                    .build();
            invalidTokenRepository.save(invalidToken);
        } catch (AppException e) {
            throw new AppException(ErrorCode.TOKEN_ALREADY_EXPIRED);
        }
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var username = signedJWT.getJWTClaimsSet().getSubject();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryTime(expirationTime)
                .build();
        invalidTokenRepository.save(invalidToken);

        UserResponse user = userMapper.toUserResponse(
                userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHETICATED)));
        String token = generateToken(user);

        return AuthenticationResponse.builder().token(token).isSuccessful(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()); // chu ky

        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier); // neu turng voi chu ky thi xac nhan
        Date expiryTime = isRefresh
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHETICATED);
        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHETICATED);
        return signedJWT;
    }

    private String generateToken(UserResponse user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("devteria.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim(
                        "refreshToken",
                        new Date(Instant.now()
                                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                                .toEpochMilli()))
                .claim("scope", buildScope(user.getRoles()).toString())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can't create token" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private StringJoiner buildScope(List<RoleResponse> roles) {
        StringJoiner scope = new StringJoiner(" ");
        roles.forEach(role -> {
            scope.add("ROLE_" + role.getCode());
            role.getPermissions().forEach(permission -> scope.add(permission.getCode()));
        });

        return scope;
    }
}
