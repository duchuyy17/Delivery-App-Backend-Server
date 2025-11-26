package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;

import com.laptrinhjavaweb.news.dto.response.mongo.CategoryDetailsResponseForMobile;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import java.util.List;

public interface CategoryService {
    RestaurantDocument createCategory(CategoryInput input);
    RestaurantDocument editCategory(CategoryInput input);
    List<CategoryDetailsResponseForMobile> fetchCategoryDetailsByStoreIdForMobile(String storeId);
}
