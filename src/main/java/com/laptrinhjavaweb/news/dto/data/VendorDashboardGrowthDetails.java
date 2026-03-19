package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class VendorDashboardGrowthDetails {
    private List<Long> totalRestaurants;
    private List<Long> totalOrders;
    private List<Double> totalSales;
}
