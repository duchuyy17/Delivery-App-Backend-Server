package com.laptrinhjavaweb.news.mapper.mongo;

import java.math.BigDecimal;

import org.bson.types.Decimal128;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantProfileInput;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantDetailResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPreview;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "commissionRate", qualifiedByName = "convertToDecimal128")
    @Mapping(target = "tax", qualifiedByName = "convertToDecimal128")
    RestaurantDocument toRestaurantDocument(RestaurantInput input);

    @Mapping(target = "location", ignore = true)
    RestaurantResponse toRestaurantResponse(RestaurantDocument restaurantDocument);

    RestaurantDetailResponse toRestaurantDetailResponse(RestaurantDocument restaurantDocument);

    @Mapping(target = "cuisines", ignore = true)
    RestaurantPreview toRestaurantPreview(RestaurantDocument restaurantDocument);

    @Mapping(target = "commissionRate", qualifiedByName = "convertToDecimal128")
    @Mapping(target = "tax", qualifiedByName = "convertToDecimal128")
    void updateRestaurant(RestaurantProfileInput restaurantInput, @MappingTarget RestaurantDocument restaurantDocument);

    @Named("convertToDecimal128")
    default Decimal128 convertToDecimal128(BigDecimal value) {
        return value == null ? null : new Decimal128(value);
    }
}
