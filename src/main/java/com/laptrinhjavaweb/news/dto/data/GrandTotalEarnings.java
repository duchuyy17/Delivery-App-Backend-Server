package com.laptrinhjavaweb.news.dto.data;

import org.bson.types.Decimal128;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GrandTotalEarnings {
    private Decimal128 platformTotal; // tổng tiền nền tảng
    private Decimal128 riderTotal; // tổng tiền tài xế
    private Decimal128 storeTotal; // tổng tiền cửa hàng
}
