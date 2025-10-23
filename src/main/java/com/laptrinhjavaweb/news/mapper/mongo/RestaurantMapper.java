package com.laptrinhjavaweb.news.mapper.mongo;


import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "location", ignore = true)
    RestaurantDocument toRestaurantDocument(RestaurantInput input);

    @Mapping(target = "location",ignore = true)
    RestaurantResponse toRestaurantResponse(RestaurantDocument restaurantDocument);
}
