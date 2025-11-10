package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.mongo.CouponDocument;
import com.laptrinhjavaweb.news.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CouponGraphQLController {
    private final CouponService couponService;
    @MutationMapping
    public CouponDocument createCoupon(@Argument("couponInput") CouponInput input) {
        return couponService.createCoupon(input);
    }
    @QueryMapping
    public List<CouponDocument> coupons() { // 👈 query này
        return couponService.getAllCoupons();
    }
    @MutationMapping
    public CouponDocument editCoupon(@Argument("couponInput") CouponInput input) { // 👈 mutation mới
        return couponService.editCoupon(input);
    }
}
