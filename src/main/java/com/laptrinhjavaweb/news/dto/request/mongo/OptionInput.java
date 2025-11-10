package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class OptionInput {
    private String title;
    private String description;
    private Double price;
}
