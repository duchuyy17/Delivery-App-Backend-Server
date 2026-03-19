package com.laptrinhjavaweb.news.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.RoleRequest;
import com.laptrinhjavaweb.news.dto.request.RoleUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;
import com.laptrinhjavaweb.news.mapper.RoleMapper;
import com.laptrinhjavaweb.news.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleMapper roleMapper;

    @Override
    public RoleResponse save(RoleRequest request) {
        return new RoleResponse();
    }

    @Override
    public void delete(Long id) {}

    @Override
    public RoleResponse update(Long id, RoleUpdateRequest request) {
        return new RoleResponse();
    }

    @Override
    public List<RoleResponse> findAll() {
        return null;
    }
}
