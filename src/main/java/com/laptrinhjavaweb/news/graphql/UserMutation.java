package com.laptrinhjavaweb.news.graphql;
import com.laptrinhjavaweb.news.mongo.UserDocument;

import com.laptrinhjavaweb.news.service.UserServiceV1;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserMutation {
    private final UserServiceV1 userService;

    @MutationMapping
    public UserDocument createUser(@Argument("input") CreateUserInput input) {
        UserDocument user = UserDocument.builder()
                .userName(input.getUserName())
                .password(input.getPassword())
                .fullName(input.getFullName())
                .email(input.getEmail())
                .imgUrl(input.getImgUrl())
                .status(input.getStatus() != null ? input.getStatus() : 1)
                .phone(input.getPhone())
                .build();

        return userService.createUser(user);
    }

    @Data
    public static class CreateUserInput {
        private String userName;
        private String password;
        private String fullName;
        private String email;
        private String phone;
        private String imgUrl;
        private Integer status;
    }
}
