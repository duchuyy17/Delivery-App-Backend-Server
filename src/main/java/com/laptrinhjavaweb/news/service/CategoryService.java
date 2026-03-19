package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;
import com.laptrinhjavaweb.news.dto.response.mongo.CategoryDetailsResponseForMobile;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface CategoryService {
    RestaurantDocument createCategory(CategoryInput input);

    RestaurantDocument editCategory(CategoryInput input);

    List<CategoryDetailsResponseForMobile> fetchCategoryDetailsByStoreIdForMobile(String storeId);
}
