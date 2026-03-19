package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.dto.response.mongo.PopularItemsResponse;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.FoodService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FoodGraphQLController {
    private final FoodService foodService;

    @MutationMapping
    public RestaurantDocument createFood(@Argument FoodInput foodInput) {
        return foodService.createFood(foodInput);
    }

    @QueryMapping
    public List<FoodDocument> popularFoodItems(@Argument String restaurantId) {
        return foodService.getPopularFoodItems(restaurantId);
    }

    @QueryMapping
    public List<PopularItemsResponse> popularItems(@Argument String restaurantId) {
        return foodService.PopularItems(restaurantId);
    }
}
