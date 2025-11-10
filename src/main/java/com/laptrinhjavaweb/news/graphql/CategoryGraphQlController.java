package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;
import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

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
}
