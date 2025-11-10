package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface FoodService {
    RestaurantDocument createFood(FoodInput input);
}
