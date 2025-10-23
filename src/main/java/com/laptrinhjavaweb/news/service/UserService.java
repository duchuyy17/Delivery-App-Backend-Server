package com.laptrinhjavaweb.news.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.laptrinhjavaweb.news.dto.ApiResponse;
import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.request.UserUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;

public interface UserService {
    ApiResponse<UserResponse> save(UserCreationRequest request);

    ApiResponse<UserResponse> findById(Long id);

    ApiResponse<UserResponse> update(long userId, UserUpdateRequest request);

    List<UserResponse> findAll(PageRequest pageRequest);

    Integer count();

    void deleteById(Long id);

    UserResponse getMyInfo();
}
