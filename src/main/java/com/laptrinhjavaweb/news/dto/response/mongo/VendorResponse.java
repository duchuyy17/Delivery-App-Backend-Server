package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import lombok.Data;

import java.util.List;

@Data
public class VendorResponse {
    private String id;
    private String email;
    private String userType;
    private List<RestaurantResponse> restaurants;

    private String get_id(){
        return id;
    }
}
