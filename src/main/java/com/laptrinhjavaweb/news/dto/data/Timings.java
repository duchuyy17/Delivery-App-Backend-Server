package com.laptrinhjavaweb.news.dto.data;

import lombok.Data;

import java.util.List;

@Data
public class Timings {
    private List<Integer> startTime; // Ví dụ: [9, 0] => 9:00
    private List<Integer> endTime;   // Ví dụ: [21, 30] => 21:30
}
