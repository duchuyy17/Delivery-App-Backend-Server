package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.mongo.AuthDataDocument;
import com.laptrinhjavaweb.news.service.UserServiceV1;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthGraphQLController {
    private final UserServiceV1 userService;
//    private final AuthenticationService<AuthenticationResponse> authenticationService;
//
//    @MutationMapping
//    public AuthenticationResponse login(@Argument String username, @Argument String password) {
//        return authenticationService.login(username, password);
//    }
    @MutationMapping
    AuthDataDocument login(@Argument String email,
                           @Argument String password,
                           @Argument String type,
                           @Argument String notificationToken) {
        return userService.login(email,password,type);
    }
}
