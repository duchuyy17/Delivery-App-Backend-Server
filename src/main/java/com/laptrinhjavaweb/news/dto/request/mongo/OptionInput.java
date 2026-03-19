package com.laptrinhjavaweb.news.dto.request.mongo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OptionInput {
    private String title;
    private String description;
    private BigDecimal price;
}
