package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.dto.response.mongo.PopularItemsResponse;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface FoodService {
    RestaurantDocument createFood(FoodInput input);

    List<FoodDocument> getPopularFoodItems(String restaurantId);

    List<PopularItemsResponse> PopularItems(String groceryId);
}
