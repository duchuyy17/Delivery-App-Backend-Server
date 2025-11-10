package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class Timings {
    private List<String> startTime; // Ví dụ: [9, 0] => 9:00
    private List<String> endTime; // Ví dụ: [21, 30] => 21:30
}
