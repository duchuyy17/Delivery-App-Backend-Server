package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.RoleRequest;
import com.laptrinhjavaweb.news.dto.request.RoleUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse save(RoleRequest request);

    void delete(Long id);

    RoleResponse update(Long id, RoleUpdateRequest request);

    List<RoleResponse> findAll();
}
