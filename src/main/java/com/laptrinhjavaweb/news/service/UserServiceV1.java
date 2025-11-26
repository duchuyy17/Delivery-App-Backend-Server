package com.laptrinhjavaweb.news.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.request.IntrospectRequest;
import com.laptrinhjavaweb.news.dto.request.LogoutRequest;
import com.laptrinhjavaweb.news.dto.request.RefreshTokenRequest;
import com.laptrinhjavaweb.news.dto.request.mongo.UserInput;
import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.dto.response.IntrospectResponse;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.UserMapperV1;
import com.laptrinhjavaweb.news.mapper.mongo.UserMapperV1Impl;
import com.laptrinhjavaweb.news.mongo.AuthDataDocument;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.repository.mongo.AuthDataRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceV1  {
    private final UserV1Repository userRepository;
    private final AuthDataRepository authDataRepository;
    private final UserMapperV1 userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    private Long REFRESH_DURATION;

    public UserDocument createUser(UserDocument user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(user);
    }

    public List<UserDocument> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDocument getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    public UserDocument emailExist(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }
    public UserDocument phoneExist(String phone) {
        return userRepository.findByPhone(phone)
                .orElse(null);
    }
    public AuthDataDocument createUser(UserInput input){
        UserDocument user = userMapper.toUserDocument(input);
        user.setUserType(UserTypeConstant.DEFAULT);
        user.setStatus(UserTypeConstant.ACTIVE);
        user.setCreatedAt(Date.from(Instant.now()));
        user.setPhoneIsVerified(true);
        user.setActive(true);
        UserDocument savedUser = userRepository.save(user);

        AuthDataDocument auth = new AuthDataDocument();
        auth.setUserId(savedUser.getId());
        Long tokenExpiriration = VALID_DURATION / (1000 * 60 * 60);
        String password = passwordEncoder.encode(user.getPassword());
        String plainPassword = user.getPassword();
        auth.setTokenExpiration(1L);
        auth.setName(input.getName());
        auth.setEmail(input.getEmail());
        auth.setPassword(password);
        auth.setPlainPassword(plainPassword);
        auth.setPhone(input.getPhone());
        auth.setUserType(UserTypeConstant.USER);
        String token = generateToken(auth);
        auth.setToken(token);
        return authDataRepository.save(auth);
    }

    private String generateToken(AuthDataDocument user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
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
                .claim("scope", "ROLE_" + user.getUserType())
                .claim("userId", user.getUserId())
                .claim("email",user.getEmail())
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
    public UserDocument updateUser(UserInput updateUserInput) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        UserDocument userDocument = getUserById(userId);
        userMapper.updateUser(updateUserInput, userDocument);
        return userRepository.save(userDocument);
    }
    public UserDocument getProfile() throws ParseException {
        String userId = jwtService.getCurrentUserId();
        return getUserById(userId);
    }


    public AuthDataDocument login(String username, String password,String type) {
        if ("default".equals(type)) {
            AuthDataDocument user =
                    authDataRepository.findByEmail(username).orElseThrow(
                            () -> new AppException(ErrorCode.USER_NOT_EXISTED));
            boolean authenticated = passwordEncoder.matches(password, user.getPassword());
            if (!authenticated) {
                throw new AppException(ErrorCode.UNAUTHETICATED);
            }

            var token = generateToken(user);

            return AuthDataDocument.builder()
                    .userId(user.getUserId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .token(token)
                    .tokenExpiration(1L)
                    .phone(user.getPhone())
                    .isNewUser(user.getIsNewUser())
                    .isActive(user.getIsActive())
                    .build();
        }
        throw new AppException(ErrorCode.LOGIN_TYPE_NOT_SUPPORT);
    }

}
