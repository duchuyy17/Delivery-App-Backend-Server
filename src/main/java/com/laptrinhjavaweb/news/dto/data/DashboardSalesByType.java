package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DashboardSalesByType {
    private Double value;
    private String label;
}
