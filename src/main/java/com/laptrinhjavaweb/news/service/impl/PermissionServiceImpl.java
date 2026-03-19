package com.laptrinhjavaweb.news.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.PermissionRequest;
import com.laptrinhjavaweb.news.dto.response.PermissionResponse;
import com.laptrinhjavaweb.news.mapper.PermissionMapper;
import com.laptrinhjavaweb.news.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class PermissionServiceImpl implements PermissionService {

    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse save(PermissionRequest request) {
        return new PermissionResponse();
    }

    @Override
    public void delete(Long id) {}

    @Override
    public List<PermissionResponse> findAll() {
        return null;
    }
}
