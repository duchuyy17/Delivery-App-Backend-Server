package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateRestaurantResponse {
    private boolean success;
    private String message;
    private RestaurantDocument data;
}
