package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import java.util.List;

public interface FoodService {
    RestaurantDocument createFood(FoodInput input);
    List<FoodDocument> getPopularFoodItems(String restaurantId);
}
