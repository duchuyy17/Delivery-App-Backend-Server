package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.*;
import com.laptrinhjavaweb.news.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardGraphQLController {
    private final DashboardService service;

    @QueryMapping
    public DashboardCounts getDashboardUsersByYear(@Argument int year) {
        return service.getDashboardUsersByYear(year);
    }

    @QueryMapping
    public VendorDashboardGrowthDetails getVendorDashboardGrowthDetailsByYear(
            @Argument String vendorId, @Argument int year) {
        return service.getGrowthDetailsByYear(vendorId, year);
    }

    @QueryMapping
    public RestaurantDashboardDetails getRestaurantDashboardSalesOrderCountDetailsByYear(
            @Argument String restaurant, @Argument int year) {
        return service.getDashboardRestaurantByYear(restaurant, year);
    }

    @QueryMapping
    public DashboardUsers getDashboardUsers() {
        return service.getDashboardUsers();
    }

    @QueryMapping
    public List<DashboardOrdersByType> getDashboardOrdersByType() {
        return service.getDashboardOrdersByType();
    }

    @QueryMapping
    public List<DashboardSalesByType> getDashboardSalesByType() {
        return service.getDashboardSalesByType();
    }

    @QueryMapping
    public RestaurantDashboardOrders getRestaurantDashboardOrdersSalesStats(
            @Argument String restaurant,
            @Argument String starting_date,
            @Argument String ending_date,
            @Argument String dateKeyword) {

        return service.getDashboardStats(restaurant, starting_date, ending_date, dateKeyword);
    }
}
