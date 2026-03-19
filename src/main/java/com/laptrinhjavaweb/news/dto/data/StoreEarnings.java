package com.laptrinhjavaweb.news.dto.data;

import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import lombok.Data;

@Data
public class StoreEarnings {
    @DBRef(lazy = true)
    private RestaurantDocument storeId; // thông tin cửa hàng

    private Decimal128 orderAmount; // tiền món
    private Decimal128 totalEarnings; // tổng cửa hàng nhận
}
