package com.laptrinhjavaweb.news.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.PermissionRequest;
import com.laptrinhjavaweb.news.dto.response.PermissionResponse;
import com.laptrinhjavaweb.news.entity.PermissionEntity;
import com.laptrinhjavaweb.news.mapper.PermissionMapper;
import com.laptrinhjavaweb.news.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse save(PermissionRequest request) {
        PermissionEntity permissionEntity = permissionRepository.save(permissionMapper.toPermissionEntity(request));
        return permissionMapper.toPermissionResponse(permissionEntity);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }

    @Override
    public List<PermissionResponse> findAll() {
        return permissionRepository.findAll().stream()
                .map(n -> permissionMapper.toPermissionResponse(n))
                .toList();
    }
}
