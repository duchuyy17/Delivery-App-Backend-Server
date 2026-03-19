package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.DateFilterInput;
import com.laptrinhjavaweb.news.dto.request.PaginationInput;
import com.laptrinhjavaweb.news.dto.request.SaveEarningRequest;
import com.laptrinhjavaweb.news.dto.response.mongo.EarningsResponse;
import com.laptrinhjavaweb.news.mongo.EarningDocument;

public interface EarningService {
    EarningsResponse getEarnings(
            String userId,
            String userType,
            String orderType,
            String paymentMethod,
            String search,
            PaginationInput pagination,
            DateFilterInput dateFilter);

    EarningDocument saveEarning(SaveEarningRequest request);
}
