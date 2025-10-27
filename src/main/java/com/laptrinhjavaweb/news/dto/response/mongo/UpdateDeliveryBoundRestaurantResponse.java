package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import lombok.Data;

@Data
public class UpdateDeliveryBoundRestaurantResponse {
    private boolean success;
    private String message;
    private RestaurantDocument data;
}
