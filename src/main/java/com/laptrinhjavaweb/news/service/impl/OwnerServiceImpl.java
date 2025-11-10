package com.laptrinhjavaweb.news.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.laptrinhjavaweb.news.service.AuthenticationService;
import com.laptrinhjavaweb.news.service.OwnerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.request.IntrospectRequest;
import com.laptrinhjavaweb.news.dto.request.LogoutRequest;
import com.laptrinhjavaweb.news.dto.request.RefreshTokenRequest;
import com.laptrinhjavaweb.news.dto.request.mongo.StaffInput;
import com.laptrinhjavaweb.news.dto.request.mongo.VendorInput;
import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.dto.response.IntrospectResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.UserMapperV1;
import com.laptrinhjavaweb.news.mapper.mongo.VendorMapper;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.repository.mongo.OwnerRepository;
import com.laptrinhjavaweb.news.util.UniqueIdUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService, AuthenticationService<OwnerDocument> {
    private final UserMapperV1 userMapper;
    private final OwnerRepository ownerRepository;
    private final VendorMapper vendorMapper;

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    private Long REFRESH_DURATION;

    @Override
    public OwnerDocument login(String username, String password) {
        OwnerDocument user =
                ownerRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(password, user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHETICATED);
        }

        var token = generateToken(user);

        return OwnerDocument.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .image(user.getImage())
                .phoneNumber(user.getPhoneNumber())
                .userType(user.getUserType())
                .userTypeId(user.getUserTypeId())
                .restaurants(user.getRestaurants())
                .permissions(user.getPermissions())
                .build();
    }

    private String generateToken(OwnerDocument user) {
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

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        return null;
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {}

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        return null;
    }

    @Override
    public String getCurrentUserId(String token) {
        return "";
    }

    @Override
    public OwnerDocument createOwner(OwnerDocument Owner) {
        try {
            Owner.setPlainPassword(Owner.getPassword());
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            Owner.setPassword(passwordEncoder.encode(Owner.getPassword()));
            if (Owner.getUserType().equals(UserTypeConstant.VENDOR)) {
                Owner.setUniqueId(UniqueIdUtil.generateVendorId());
            }
            return ownerRepository.save(Owner);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create staff", e);
        }
    }

    @Override
    public List<OwnerDocument> getVendors() {
        return ownerRepository.findByUserType(UserTypeConstant.VENDOR);
    }

    @Override
    public List<OwnerDocument> getStaffs() {
        return ownerRepository.findByUserType(UserTypeConstant.STAFF);
    }

    @Override
    public VendorResponse getVendor(String id) {
        return vendorMapper.toVendorResponse(
                ownerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_EXISTED)));
    }

    @Override
    public OwnerDocument getVendorDocument(String id) {
        return ownerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_EXISTED));
    }

    @Override
    public OwnerDocument updateOwner(VendorInput input) {

        Optional<OwnerDocument> optionalVendor = ownerRepository.findById(input.get_id());
        if (optionalVendor.isEmpty()) {
            throw new AppException(ErrorCode.VENDOR_NOT_EXISTED);
        }

        OwnerDocument vendor = optionalVendor.get();
        vendorMapper.updateOwner(input, vendor);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        vendor.setPassword(passwordEncoder.encode(input.getPassword()));
        vendor.setPlainPassword(input.getPassword());
        return ownerRepository.save(vendor);
    }

    @Override
    public OwnerDocument editStaff(StaffInput input) {
        OwnerDocument existing =
                ownerRepository.findById(input.getId()).orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND));
        vendorMapper.updateStaff(input, existing);
        return ownerRepository.save(existing);
    }
}
