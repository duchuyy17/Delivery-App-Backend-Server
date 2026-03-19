package com.laptrinhjavaweb.news.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrafficRequest {
    private double restaurantLat;
    private double restaurantLng;
    private double customerLat;
    private double customerLng;
}
