package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.DateFilterInput;
import com.laptrinhjavaweb.news.dto.request.PaginationInput;
import com.laptrinhjavaweb.news.dto.request.SaveEarningRequest;
import com.laptrinhjavaweb.news.dto.response.SaveEarningResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.EarningsResponse;
import com.laptrinhjavaweb.news.mongo.EarningDocument;
import com.laptrinhjavaweb.news.service.EarningService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EarningGraphQLController {
    private final EarningService earningService;

    @QueryMapping
    public EarningsResponse earnings(
            @Argument String userId,
            @Argument String userType,
            @Argument String orderType,
            @Argument String paymentMethod,
            @Argument String search,
            @Argument PaginationInput pagination,
            @Argument DateFilterInput dateFilter) {

        return earningService.getEarnings(userId, userType, orderType, paymentMethod, search, pagination, dateFilter);
    }

    @MutationMapping
    public SaveEarningResponse saveEarning(@Argument("input") SaveEarningRequest input) {
        EarningDocument saved = earningService.saveEarning(input);

        return SaveEarningResponse.builder()
                .success(true)
                .message("Save earning successfully")
                .data(saved)
                .build();
    }
}
