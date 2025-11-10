package com.laptrinhjavaweb.news.dto.response.mongo;

import java.util.List;

import lombok.Data;

@Data
public class VendorResponse {
    private String id;
    private String email;
    private String userType;
    private List<RestaurantResponse> restaurants;

    private String get_id() {
        return id;
    }
}
