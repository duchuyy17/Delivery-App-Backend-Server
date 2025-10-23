package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class PointInput {
    private List<Double> coordinates;
    // Getters and setters
}
