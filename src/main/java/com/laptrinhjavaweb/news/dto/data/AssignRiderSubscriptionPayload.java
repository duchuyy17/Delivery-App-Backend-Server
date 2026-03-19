package com.laptrinhjavaweb.news.dto.data;

import com.laptrinhjavaweb.news.mongo.OrderDocument;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRiderSubscriptionPayload {
    private OrderDocument order;
    private String origin;
}
