package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;
import com.laptrinhjavaweb.news.service.CuisineService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CuisineGraphQLController {
    private final CuisineService cuisineService;

    @MutationMapping
    CuisineDocument createCuisine(@Argument("cuisineInput") CuisineDocument cuisineInput) {
        return cuisineService.createCuisine(cuisineInput);
    }

    @QueryMapping
    List<CuisineDocument> cuisines() {
        return cuisineService.getAllCuisine();
    }
}
