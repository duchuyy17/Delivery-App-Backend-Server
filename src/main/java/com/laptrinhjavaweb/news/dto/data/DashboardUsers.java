package com.laptrinhjavaweb.news.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardUsers {
    private long usersCount;
    private long vendorsCount;
    private long restaurantsCount;
    private long ridersCount;
}
