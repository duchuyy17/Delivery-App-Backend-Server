package com.laptrinhjavaweb.news.graphql;

import java.text.ParseException;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.service.AddressService;
import com.laptrinhjavaweb.news.service.JwtService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AddressGraphQLController {
    private final AddressService addressService;
    private final JwtService jwtService;

    @MutationMapping
    public UserDocument createAddress(@Argument AddressInput addressInput) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        return addressService.createAddress(userId, addressInput);
    }

    @MutationMapping
    public UserDocument selectAddress(@Argument String id) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        return addressService.selectAddress(userId, id);
    }
}
