package com.laptrinhjavaweb.news.mapper.mongo;

import java.math.BigDecimal;

import org.bson.types.Decimal128;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.laptrinhjavaweb.news.dto.request.mongo.CouponInput;
import com.laptrinhjavaweb.news.mongo.CouponDocument;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    @Mapping(target = "discount", qualifiedByName = "convertToDecimal128")
    void updateCoupon(CouponInput couponInput, @MappingTarget CouponDocument couponDocument);

    @Named("convertToDecimal128")
    default Decimal128 convertToDecimal128(BigDecimal value) {
        return value == null ? null : new Decimal128(value);
    }
}
