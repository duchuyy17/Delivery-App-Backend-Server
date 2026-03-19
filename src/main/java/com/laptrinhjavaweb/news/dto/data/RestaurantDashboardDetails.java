package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class RestaurantDashboardDetails {
    private List<Integer> salesAmount;
    private List<Double> ordersCount;
}
