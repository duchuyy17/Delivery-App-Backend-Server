package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.dto.data.DeliveryInfo;
import com.laptrinhjavaweb.news.dto.request.mongo.PointInput;

import lombok.Data;

@Data
public class RestaurantResponse {

    private String uniqueRestaurantId;
    private String id;
    private String orderId;
    private String orderPrefix;
    private String name;
    private String slug;
    private String image;
    private String address;
    private Boolean isActive;
    private Integer deliveryTime;
    private Integer minimumOrder;
    private String username;
    private String password;
    private DeliveryInfo deliveryInfo;
    private String shopType;
    private PointInput location;

    public String get_id() {
        return id;
    }

    public String getunique_restaurant_id() {
        return uniqueRestaurantId;
    }
}
