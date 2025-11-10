package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface CategoryService {
    RestaurantDocument createCategory(CategoryInput input);
    RestaurantDocument editCategory(CategoryInput input);
}
