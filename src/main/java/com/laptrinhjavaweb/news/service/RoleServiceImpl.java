package com.laptrinhjavaweb.news.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.RoleRequest;
import com.laptrinhjavaweb.news.dto.request.RoleUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;
import com.laptrinhjavaweb.news.entity.PermissionEntity;
import com.laptrinhjavaweb.news.entity.RoleEntity;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.RoleMapper;
import com.laptrinhjavaweb.news.repository.PermissionRepository;
import com.laptrinhjavaweb.news.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse save(RoleRequest request) {
        RoleEntity roleEntity = roleMapper.toRoleEntity(request);
        List<PermissionEntity> permissions = permissionRepository.findAllById(request.getPermissions());
        roleEntity.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(roleEntity));
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponse update(Long id, RoleUpdateRequest request) {
        List<PermissionEntity> permissionEntities = permissionRepository.findAllById(request.getPermissions());
        RoleEntity roleEntity = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.UNFIND_ROLE));
        roleMapper.updateRoleEntity(roleEntity, request);
        roleEntity.setPermissions(new HashSet<>(permissionEntities));

        return roleMapper.toRoleResponse(roleRepository.save(roleEntity));
    }

    @Override
    public List<RoleResponse> findAll() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return roleEntities.stream().map(roleMapper::toRoleResponse).toList();
    }
}
