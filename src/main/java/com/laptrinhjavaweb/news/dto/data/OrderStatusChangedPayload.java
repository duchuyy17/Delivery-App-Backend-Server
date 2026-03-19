package com.laptrinhjavaweb.news.dto.data;

import com.laptrinhjavaweb.news.mongo.OrderDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusChangedPayload {
    private String userId; // user được notify
    private String origin; // "new" | "update"
    private OrderDocument order;
}
