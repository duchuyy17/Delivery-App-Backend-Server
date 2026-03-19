package com.laptrinhjavaweb.news.dto.data;

import com.laptrinhjavaweb.news.mongo.OrderDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchOrderSubscriptionPayload {
    private String riderId;
    private String origin;
    private OrderDocument order;
}
