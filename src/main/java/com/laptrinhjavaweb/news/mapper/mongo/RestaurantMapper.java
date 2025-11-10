package com.laptrinhjavaweb.news.mapper.mongo;

import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPreview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantProfileInput;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "location", ignore = true)
    RestaurantDocument toRestaurantDocument(RestaurantInput input);

    @Mapping(target = "location", ignore = true)
    RestaurantResponse toRestaurantResponse(RestaurantDocument restaurantDocument);

    RestaurantPreview toRestaurantPreview(RestaurantDocument restaurantDocument);

    void updateRestaurant(RestaurantProfileInput restaurantInput, @MappingTarget RestaurantDocument restaurantDocument);
}
