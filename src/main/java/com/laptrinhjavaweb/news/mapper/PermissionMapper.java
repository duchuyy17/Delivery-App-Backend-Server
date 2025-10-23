package com.laptrinhjavaweb.news.mapper;

import org.mapstruct.Mapper;

import com.laptrinhjavaweb.news.dto.request.PermissionRequest;
import com.laptrinhjavaweb.news.dto.response.PermissionResponse;
import com.laptrinhjavaweb.news.entity.PermissionEntity;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionEntity toPermissionEntity(PermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(PermissionEntity permissionEntity);
}
