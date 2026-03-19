package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class DashboardCounts {
    private List<Integer> usersCount;
    private List<Integer> vendorsCount;
    private List<Integer> restaurantsCount;
    private List<Integer> ridersCount;

    private PercentageChange percentageChange;
}
