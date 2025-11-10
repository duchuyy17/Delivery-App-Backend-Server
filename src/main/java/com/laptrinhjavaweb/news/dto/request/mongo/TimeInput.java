package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class TimeInput {
    private List<String> startTime;
    private List<String> endTime;
}
