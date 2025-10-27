package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.request.mongo.UpdateDeliveryBoundsInput;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateDeliveryBoundRestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RestaurantService {
    RestaurantDocument createRestaurant(RestaurantInput restaurantInput,String ownerId);
    List<RestaurantDocument> getAllRestaurants(PageRequest pageable);
    Long count();
    UpdateDeliveryBoundRestaurantResponse updateDeliveryBoundsAndLocation(UpdateDeliveryBoundsInput input);
    RestaurantDocument findById(String id);
    RestaurantDocument getRestaurantDeliveryZoneInfo(String id);
}
