package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.PermissionRequest;
import com.laptrinhjavaweb.news.dto.response.PermissionResponse;

public interface PermissionService {
    PermissionResponse save(PermissionRequest request);

    void delete(Long id);

    List<PermissionResponse> findAll();
}
