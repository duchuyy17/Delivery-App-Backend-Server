package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.dto.response.mongo.CategoryDetailsResponseForMobile;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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
}
