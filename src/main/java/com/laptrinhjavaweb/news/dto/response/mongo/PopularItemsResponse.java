package com.laptrinhjavaweb.news.dto.response.mongo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopularItemsResponse {
    private String id; // trực tiếp foodId
    private Long count;
}
