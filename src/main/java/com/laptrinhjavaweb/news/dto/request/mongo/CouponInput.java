package com.laptrinhjavaweb.news.dto.request.mongo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CouponInput {
    private String _id;
    private String id;
    private String title;
    private BigDecimal discount;
    private boolean enabled;

    public String getId() {
        return _id;
    }
}
