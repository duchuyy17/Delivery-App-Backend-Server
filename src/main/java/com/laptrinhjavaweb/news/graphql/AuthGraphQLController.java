package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.response.AuthenticationResponse;
import com.laptrinhjavaweb.news.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthGraphQLController {

    private final AuthenticationService<AuthenticationResponse> authenticationService;

    @MutationMapping
    public AuthenticationResponse login(@Argument String username, @Argument String password) {
        return authenticationService.login(username, password);
    }
}
