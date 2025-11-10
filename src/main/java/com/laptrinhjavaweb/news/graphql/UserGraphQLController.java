package com.laptrinhjavaweb.news.graphql;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.UserInput;
import com.laptrinhjavaweb.news.mongo.AuthDataDocument;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.CoordinatesInput;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.service.UserServiceV1;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserServiceV1 userService;

    @MutationMapping
    public AuthDataDocument createUser(@Argument("userInput") UserInput userInput) {
        return userService.createUser(userInput);
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

    @QueryMapping
    public UserDocument user(@Argument("id") String id) {
        return userService.getUserById(id);
    }
    @MutationMapping
    UserDocument emailExist(@Argument String email){
        return userService.emailExist(email);
    }

    @MutationMapping
    UserDocument phoneExist(@Argument String phone){
        return userService.phoneExist(phone);
    }

    @MutationMapping
    public UserDocument updateUser(@Argument UserInput updateUserInput) throws ParseException {
        return userService.updateUser( updateUserInput);
    }

    @QueryMapping
    public UserDocument profile() throws ParseException {
        return userService.getProfile();
    }

}
