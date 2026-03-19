package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.mongo.CouponDocument;

public interface CouponService {
    CouponDocument createCoupon(CouponInput input);

    List<CouponDocument> getAllCoupons();

    CouponDocument editCoupon(CouponInput input);
}
