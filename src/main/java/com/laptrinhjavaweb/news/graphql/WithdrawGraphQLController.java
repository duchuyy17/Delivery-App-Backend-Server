package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.WithdrawResponseDTO;
import com.laptrinhjavaweb.news.dto.request.PaginationInput;
import com.laptrinhjavaweb.news.service.WithdrawService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WithdrawGraphQLController {
    private final WithdrawService withdrawService;

    @QueryMapping
    public WithdrawResponseDTO withdrawRequests(
            @Argument String userType,
            @Argument String userId,
            @Argument PaginationInput pagination,
            @Argument String search) {
        return withdrawService.getWithdrawRequests(
                userType, userId, pagination.getPageSize(), pagination.getPageNo(), search);
    }
}
