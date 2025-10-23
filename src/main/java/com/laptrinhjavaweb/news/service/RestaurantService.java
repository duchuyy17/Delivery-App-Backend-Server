package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RestaurantService {
    RestaurantDocument createRestaurant(RestaurantInput restaurantInput,String ownerId);
    List<RestaurantDocument> getAllRestaurants(PageRequest pageable);
    Long count();
}
