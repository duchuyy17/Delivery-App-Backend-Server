package com.laptrinhjavaweb.news.dto.request;

import lombok.Data;

@Data
public class PaginationInput {
    private int pageSize;
    private int pageNo;
}
