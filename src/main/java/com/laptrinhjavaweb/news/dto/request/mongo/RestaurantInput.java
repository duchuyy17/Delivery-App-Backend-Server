package com.laptrinhjavaweb.news.dto.request.mongo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RestaurantInput {
    private String id;
    private String _id;
    private String name;
    private String image;
    private String username;
    private String orderPrefix;
    private String slug;
    private String phone;
    private String address;
    private int deliveryTime;
    private Integer minimumOrder;
    private boolean isActive;
    private BigDecimal commissionRate;
    private BigDecimal tax;
    private BigDecimal salesTax;
    private String shopType;
    private int orderId;
    private String logo;
    private String password;
    private List<String> cuisines;
    private PointInput location;

    public String getId() {
        return _id != null ? _id : id;
    }

    public BigDecimal getTax() {
        return salesTax;
    }
}
