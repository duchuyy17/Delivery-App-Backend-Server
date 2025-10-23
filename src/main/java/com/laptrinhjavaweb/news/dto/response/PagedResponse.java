package com.laptrinhjavaweb.news.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {
    private List<T> data;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int limit;
}
