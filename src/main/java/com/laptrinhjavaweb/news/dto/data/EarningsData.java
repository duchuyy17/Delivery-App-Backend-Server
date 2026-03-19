package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import com.laptrinhjavaweb.news.mongo.EarningDocument;

import lombok.Data;

@Data
public class EarningsData {
    private List<EarningDocument> earnings;
    private GrandTotalEarnings grandTotalEarnings;
}
