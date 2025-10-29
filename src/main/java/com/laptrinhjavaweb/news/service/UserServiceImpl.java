package com.laptrinhjavaweb.news.service;

import java.util.*;

import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.ApiResponse;
import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.request.UserUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.entity.RoleEntity;
import com.laptrinhjavaweb.news.entity.UserEntity;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.UserMapper;
import com.laptrinhjavaweb.news.repository.RoleRepository;
import com.laptrinhjavaweb.news.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;
    UserV1Repository repository;

    UserMapper userMapper;

    @Override
    public ApiResponse<UserResponse> save(UserCreationRequest request) {
        log.info("execute in service");

        UserEntity user = userMapper.toUserEntity(request);

        List<RoleEntity> roles = roleRepository.findAllById(request.getRoleCode());

        user.setRoles(new HashSet<>(roles));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        try {
            UserEntity userEntity = userRepository.save(user);
            UserResponse userResponse = userMapper.toUserResponse(userEntity);
            apiResponse.setResult(userResponse);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return apiResponse;
    }

    @Override
    public ApiResponse<UserResponse> findById(Long id) {

        Optional<UserEntity> user = userRepository.findById(id);
        UserResponse userResponse =
                userMapper.toUserResponse(user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));

        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @Override
    public ApiResponse<UserResponse> update(long userId, UserUpdateRequest request) {
        Optional<UserEntity> user = userRepository.findById(userId);
        userMapper.updateUserEntity(user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)), request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userMapper.toUserResponse(
                userRepository.save(user.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))));
        return apiResponse;
    }

    @Override
    public List<UserResponse> findAll(PageRequest pageRequest) {
        List<UserResponse> userResponseList;
        userResponseList = userRepository.findAll(pageRequest).stream()
                .map(userMapper::toUserResponse)
                .toList();
        return userResponseList;
    }

    @Override
    public Integer count() {
        List<UserResponse> userResponseList;
        userResponseList = userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
        return userResponseList.size();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity user =
                userRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.UNAUTHETICATED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserDocument> findAll() {
        return repository.findAll();
    }
}
