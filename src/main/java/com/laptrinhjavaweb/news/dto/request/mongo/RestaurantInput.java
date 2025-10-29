package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantInput {
    private String name;
    private String image;
    private String username;
    private String orderPrefix;
    private String slug;
    private String phone;
    private String address;
    private int deliveryTime;
    private int minimumOrder;
    private boolean isActive;
    private double commissionRate;
    private double tax;
    private double salesTax;
    private String shopType;
    private int orderId;
    private String logo;
    private String password;
    private List<String> cuisines;
    private PointInput location;
}
