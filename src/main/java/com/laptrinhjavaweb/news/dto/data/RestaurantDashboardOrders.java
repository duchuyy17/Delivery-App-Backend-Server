package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDashboardOrders {
    private Integer totalOrders;
    private Double totalSales;
    private Integer totalCODOrders;
    private Integer totalCardOrders;
}
