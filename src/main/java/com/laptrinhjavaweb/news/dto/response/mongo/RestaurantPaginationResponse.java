package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RestaurantPaginationResponse {
    private List<RestaurantDocument> data;
    private Long totalCount;
    private Integer currentPage;
    private Long totalPages;
}
