package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;
import com.laptrinhjavaweb.news.dto.response.mongo.CategoryDetailsResponseForMobile;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CategoryGraphQlController {
    private final CategoryService categoryService;

    @MutationMapping
    public RestaurantDocument createCategory(@Argument("category") CategoryInput input) {
        return categoryService.createCategory(input);
    }

    @MutationMapping
    public RestaurantDocument editCategory(@Argument("category") CategoryInput category) {
        return categoryService.editCategory(category);
    }

    @QueryMapping
    public List<CategoryDetailsResponseForMobile> fetchCategoryDetailsByStoreIdForMobile(@Argument String storeId) {
        return categoryService.fetchCategoryDetailsByStoreIdForMobile(storeId);
    }
}
