package com.laptrinhjavaweb.news.mapper.mongo;

import com.laptrinhjavaweb.news.dto.request.mongo.UserInput;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.request.UserUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.mongo.UserDocument;

@Mapper(componentModel = "spring")
public interface UserMapperV1 {
    UserDocument toUserEntity(UserCreationRequest userCreationRequest);

    UserDocument toUserDocument(UserInput userInput);

    void updateUser(UserInput userInput,@MappingTarget UserDocument userDocument);

    UserResponse toUserResponse(UserDocument userEntity);

    void updateUserEntity(@MappingTarget UserDocument entity, UserUpdateRequest request);
}
