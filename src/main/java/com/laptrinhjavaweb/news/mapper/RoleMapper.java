package com.laptrinhjavaweb.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.RoleRequest;
import com.laptrinhjavaweb.news.dto.request.RoleUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;
import com.laptrinhjavaweb.news.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    RoleEntity toRoleEntity(RoleRequest roleRequest);

    RoleResponse toRoleResponse(RoleEntity roleEntity);

    @Mapping(target = "permissions", ignore = true)
    void updateRoleEntity(@MappingTarget RoleEntity entity, RoleUpdateRequest request);
}
