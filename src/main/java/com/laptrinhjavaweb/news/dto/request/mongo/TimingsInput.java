package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class TimingsInput {
    private String day;
    private List<TimeInput> times;
}
