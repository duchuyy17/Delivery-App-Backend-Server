package com.laptrinhjavaweb.news.dto.request.mongo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class VariationInput {
    private String title;
    private List<String> addons;
    private boolean isOutOfStock;
    private BigDecimal price;
    private BigDecimal discounted;
}
