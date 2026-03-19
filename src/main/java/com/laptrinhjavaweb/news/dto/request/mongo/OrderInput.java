package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class OrderInput {
    private String food;
    private Integer quantity;
    private String variation;
    private List<AddonInput> addons;
    private String specialInstructions;
}
