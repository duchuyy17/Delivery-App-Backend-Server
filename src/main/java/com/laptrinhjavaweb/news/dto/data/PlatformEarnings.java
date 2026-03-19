package com.laptrinhjavaweb.news.dto.data;

import org.bson.types.Decimal128;

import lombok.Data;

@Data
public class PlatformEarnings {
    private Decimal128 marketplaceCommission; // hoa hồng cửa hàng
    private Decimal128 deliveryCommission; // phí dịch vụ từ tài xế
    private Decimal128 tax; // thuế
    private Decimal128 platformFee; // phí nền tảng
    private Decimal128 totalEarnings; // tổng nền tảng thu
}
