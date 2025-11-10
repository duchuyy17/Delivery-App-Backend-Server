package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class AddonInput {
    private String title;
    private String description;
    private Integer quantityMaximum;
    private Integer quantityMinimum;
    private List<String> options;
}
