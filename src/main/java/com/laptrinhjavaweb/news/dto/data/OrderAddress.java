package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddress {
    private String id;
    private String deliveryAddress;
    private String details;
    private String label;
    private Point location;
}
