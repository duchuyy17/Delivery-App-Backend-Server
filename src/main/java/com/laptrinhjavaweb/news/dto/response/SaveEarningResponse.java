package com.laptrinhjavaweb.news.dto.response;

import com.laptrinhjavaweb.news.mongo.EarningDocument;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveEarningResponse {
    private Boolean success;
    private String message;
    private EarningDocument data;
}
