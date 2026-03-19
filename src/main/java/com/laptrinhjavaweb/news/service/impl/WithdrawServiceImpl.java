package com.laptrinhjavaweb.news.service.impl;

import java.util.Collections;
import java.util.Date;

import org.bson.types.Decimal128;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.BussinessDetails;
import com.laptrinhjavaweb.news.dto.data.PaginationDTO;
import com.laptrinhjavaweb.news.dto.data.WithdrawResponseDTO;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.WithdrawRequestDocument;
import com.laptrinhjavaweb.news.service.WithdrawService;

@Service
public class WithdrawServiceImpl implements WithdrawService {
    @Override
    public WithdrawResponseDTO getWithdrawRequests(
            String userType, String userId, int pageSize, int pageNo, String search) {
        // MOCK DATA
        BussinessDetails business = new BussinessDetails();
        business.setBankName("Vietcombank");
        business.setAccountName("Nguyen Van A");
        business.setAccountCode("VCB001");
        business.setAccountNumber("0123456789");
        business.setBussinessRegNo("BR001");
        business.setCompanyRegNo("CR001");
        business.setTaxRate(5.0);

        RiderDocument rider = new RiderDocument();
        rider.setId("rider123");
        rider.setName("Bike Driver A");
        rider.setPhone("0912345678");
        rider.setAvailable(true);
        rider.setActive(true);

        rider.setCurrentWalletAmount(new Decimal128(150000));
        rider.setTotalWalletAmount(new Decimal128(500000));
        rider.setWithdrawnWalletAmount(new Decimal128(300000));
        rider.setUsername("driverA");
        rider.setBussinessDetails(business);

        RestaurantDocument store = new RestaurantDocument();
        store.setId("store001");
        store.setUniqueRestaurantId("rest001");
        store.setImage("image.png");
        store.setLogo("logo.png");
        store.setAddress("HCM City");
        store.setUsername("shopA");
        store.setPassword("123456");
        store.setSlug("shop-a");
        store.setStripeDetailsSubmitted(true);
        store.setCommissionRate(new Decimal128(12));
        store.setBussinessDetails(business);

        WithdrawRequestDocument req = new WithdrawRequestDocument();
        req.setId("wr001");
        req.setRequestId("REQ-0001");
        req.setRequestAmount(200000.0);
        req.setRequestTime(new Date());
        req.setStatus("PENDING");
        req.setCreatedAt(new Date());
        req.setRider(rider);
        req.setStore(store);

        PaginationDTO pagination = new PaginationDTO();
        pagination.setTotal(1);

        WithdrawResponseDTO response = new WithdrawResponseDTO();
        response.setMessage("Success");
        response.setPagination(pagination);
        response.setData(Collections.singletonList(req));
        response.setSuccess(true);

        return response;
    }
}
