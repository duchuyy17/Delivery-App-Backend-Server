package com.laptrinhjavaweb.news.graphql;
import com.cloudinary.Coordinates;
import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.data.Address;
import com.laptrinhjavaweb.news.dto.request.mongo.CoordinatesInput;
import com.laptrinhjavaweb.news.mongo.UserDocument;

import com.laptrinhjavaweb.news.service.UserServiceV1;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {
    private final UserServiceV1 userService;

    @MutationMapping
    public UserDocument createUser(@Argument("input") CreateUserInput input) {
        UserDocument user = UserDocument.builder()
                .userName(input.getUserName())
                .password(input.getPassword())
                .name(input.getName())
                .fullName(input.getFullName())
                .email(input.getEmail())
                .imgUrl(input.getImgUrl())
                .phone(input.getPhone())
                .build();
        user.setStatus(UserTypeConstant.ACTIVE);
        user.setUserType(UserTypeConstant.DEFAULT);
        user.setCreatedAt(new Date());
        return userService.createUser(user);
    }

    @Data
    public static class CreateUserInput {
        private String userName;
        private String password;
        private String fullName;
        private String name;
        private String email;
        private String phone;
        private String imgUrl;
        private String status;
        private String userType;
        private CoordinatesInput location;

    }

    @QueryMapping
    public List<UserDocument> users() {
        List<UserDocument> users = new ArrayList<>();
        users = userService.getAllUsers();
        return users;
    }
}
