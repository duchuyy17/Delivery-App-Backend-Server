package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import com.laptrinhjavaweb.news.mongo.WithdrawRequestDocument;

import lombok.Data;

@Data
public class WithdrawResponseDTO {
    private String message;
    private PaginationDTO pagination;
    private List<WithdrawRequestDocument> data;
    private boolean success;
}
