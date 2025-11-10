package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class PointInput {
    private List<Double> coordinates;
    // Getters and setters
}
