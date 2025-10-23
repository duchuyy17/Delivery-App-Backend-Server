package com.laptrinhjavaweb.news.dto.data;

import lombok.Data;

@Data
public class DeliveryInfo {
    private Long minDeliveryFee;
    private Double deliveryDistance;
    private Long deliveryFee;
}
