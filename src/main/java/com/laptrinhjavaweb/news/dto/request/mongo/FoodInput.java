package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class FoodInput {
    private String title;
    private String description;
    private String subCategory;
    private String category;
    private String restaurant;
    private List<VariationInput> variations;
    private String image;
    private boolean isActive;
    private boolean isOutOfStock;
}
