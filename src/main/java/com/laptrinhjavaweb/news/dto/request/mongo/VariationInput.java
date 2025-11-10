package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class VariationInput {
    private String title;
    private List<String> addons;
    private boolean isOutOfStock;
    private Float price;
    private Float discounted;
}
