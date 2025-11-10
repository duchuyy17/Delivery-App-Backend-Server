package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class OpeningTimes {
    private String day; // Ví dụ: "MON", "TUE", "WED"...
    private List<Timings> times; // Một ngày có thể có nhiều khung giờ mở cửa
}
