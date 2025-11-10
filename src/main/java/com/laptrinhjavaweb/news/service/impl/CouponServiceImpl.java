package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.CouponMapper;
import com.laptrinhjavaweb.news.mongo.CouponDocument;
import com.laptrinhjavaweb.news.repository.mongo.CouponRepository;
import com.laptrinhjavaweb.news.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;
    @Override
    public CouponDocument createCoupon(CouponInput input) {
        CouponDocument coupon = new CouponDocument();
        coupon.setTitle(input.getTitle());
        coupon.setDiscount(input.getDiscount());
        coupon.setEnabled(input.isEnabled());
        return couponRepository.save(coupon);
    }

    @Override
    public List<CouponDocument> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    public CouponDocument editCoupon(CouponInput input) {
        CouponDocument coupon = couponRepository.findById(input.getId())
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));
        couponMapper.updateCoupon(input, coupon);
        return couponRepository.save(coupon);
    }
}
