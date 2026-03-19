package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.data.*;

public interface DashboardService {
    DashboardCounts getDashboardUsersByYear(int year);

    VendorDashboardGrowthDetails getGrowthDetailsByYear(String vendorId, int year);

    RestaurantDashboardDetails getDashboardRestaurantByYear(String restaurantId, int year);

    DashboardUsers getDashboardUsers();

    List<DashboardOrdersByType> getDashboardOrdersByType();

    List<DashboardSalesByType> getDashboardSalesByType();

    RestaurantDashboardOrders getDashboardStats(String restaurantId, String start, String end, String dateKeyword);
}
