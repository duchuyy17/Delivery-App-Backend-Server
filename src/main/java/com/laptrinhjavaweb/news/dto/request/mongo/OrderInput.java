package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class OrderInput {
    private String food;
    private Integer quantity;
    private String variation;
    private List<AddonInput> addons;
    private String specialInstructions;
}
