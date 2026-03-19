package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DashboardOrdersByType {
    private long value;
    private String label;
}
