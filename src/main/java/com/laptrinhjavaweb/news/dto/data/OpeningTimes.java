package com.laptrinhjavaweb.news.dto.data;

import lombok.Data;

import java.util.List;

@Data
public class OpeningTimes {
    private String day;              // Ví dụ: "MON", "TUE", "WED"...
    private List<Timings> times;     // Một ngày có thể có nhiều khung giờ mở cửa
}
