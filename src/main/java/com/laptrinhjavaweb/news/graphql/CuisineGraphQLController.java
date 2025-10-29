package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;
import com.laptrinhjavaweb.news.service.CuisineService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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
