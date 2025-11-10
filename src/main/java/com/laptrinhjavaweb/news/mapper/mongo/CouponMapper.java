package com.laptrinhjavaweb.news.mapper.mongo;


import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.mongo.CouponDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    void updateCoupon(CouponInput couponInput, @MappingTarget CouponDocument couponDocument);
}
