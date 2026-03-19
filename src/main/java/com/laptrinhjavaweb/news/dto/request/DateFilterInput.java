package com.laptrinhjavaweb.news.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateFilterInput {
    private String starting_date;
    private String ending_date;
}
