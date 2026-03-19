package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.data.WithdrawResponseDTO;

public interface WithdrawService {

    WithdrawResponseDTO getWithdrawRequests(String userType, String userId, int pageSize, int pageNo, String search);
}
