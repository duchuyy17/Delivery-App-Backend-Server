package com.laptrinhjavaweb.news.dto.response.mongo;

import java.util.List;

import com.laptrinhjavaweb.news.mongo.OrderDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPageDTO {
    private long totalCount;
    private List<OrderDocument> orders;
}
