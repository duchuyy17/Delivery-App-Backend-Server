package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.mongo.CouponDocument;

import java.util.List;

public interface CouponService {
    CouponDocument createCoupon(CouponInput input);
    List<CouponDocument> getAllCoupons();
    CouponDocument editCoupon(CouponInput input);
}
