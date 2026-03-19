package com.laptrinhjavaweb.news.dto.request;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveEarningRequest {
    private String orderId;
    private String orderType;
    private String paymentMethod;

    // Platform
    private BigDecimal marketplaceCommission;
    private BigDecimal deliveryCommission;
    private BigDecimal tax;
    private BigDecimal platformFee;

    // Rider
    private String riderId;
    private BigDecimal deliveryFee;
    private BigDecimal tip;

    // Store
    private String storeId;
    private BigDecimal orderAmount;
}
