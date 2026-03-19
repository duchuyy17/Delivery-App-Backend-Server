package com.laptrinhjavaweb.news.dto.data;

import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

import lombok.Data;

@Data
public class RiderEarnings {
    @DBRef(lazy = true)
    private RiderDocument riderId; // thông tin tài xế

    private Decimal128 deliveryFee; // phí giao hàng
    private Decimal128 tip; // tiền tip
    private Decimal128 totalEarnings; // tổng tài xế nhận
}
