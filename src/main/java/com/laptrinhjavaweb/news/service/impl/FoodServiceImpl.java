package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.FoodInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.mongo.VariationDocument;
import com.laptrinhjavaweb.news.repository.mongo.CategoryRepository;
import com.laptrinhjavaweb.news.repository.mongo.FoodRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.repository.mongo.VariationRepository;
import com.laptrinhjavaweb.news.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final VariationRepository variationRepository;
    @Override
    public RestaurantDocument createFood(FoodInput input) {
        // Lấy thông tin nhà hàng
        RestaurantDocument restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        // Lấy category chứa món ăn
        CategoryDocument category = categoryRepository.findById(input.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Map variations
        List<VariationDocument> variations = input.getVariations().stream()
                .map(v -> VariationDocument.builder()
                        .title(v.getTitle())
                        .price(v.getPrice())
                        .discounted(v.getDiscounted() != null ? v.getDiscounted() : 0)
                        .addons(v.getAddons())
                        .isOutOfStock(v.isOutOfStock())
                        .build())
                .toList();
        List<VariationDocument> variationSaved = variationRepository.saveAll(variations);
        // Tạo Food mới
        FoodDocument food = FoodDocument.builder()
                .title(input.getTitle())
                .description(input.getDescription())
                .subCategory(input.getSubCategory())
                .variations(variationSaved)
                .image(input.getImage())
                .restaurant(input.getRestaurant())
                .isActive(input.isActive())
                .isOutOfStock(input.isOutOfStock())
                .build();
        FoodDocument foodSaved = foodRepository.save(food);
        List<FoodDocument> foodSavedList = new ArrayList<>();
        foodSavedList.add(foodSaved);

        if (category.getFoods() == null || category.getFoods().isEmpty()) {
            category.setFoods(foodSavedList);
        } else {
            category.getFoods().addAll(foodSavedList);
        }
        categoryRepository.save(category);
        return restaurant;
    }
}
