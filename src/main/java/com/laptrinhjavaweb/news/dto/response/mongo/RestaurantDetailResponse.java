package com.laptrinhjavaweb.news.dto.response.mongo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDetailResponse {
    private String _id;
    private String name;
    private String address;
    private String image;
    private LocationResponse location;

}
