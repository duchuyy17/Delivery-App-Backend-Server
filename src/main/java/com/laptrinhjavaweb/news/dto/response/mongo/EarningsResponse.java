package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.dto.data.EarningsData;
import com.laptrinhjavaweb.news.dto.data.Pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EarningsResponse {
    private Boolean success;
    private String message;
    private EarningsData data;
    private Pagination pagination;
}
