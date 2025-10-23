package com.laptrinhjavaweb.news.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.request.UserUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(UserEntity userEntity);

    void updateUserEntity(@MappingTarget UserEntity entity, UserUpdateRequest request);
}
