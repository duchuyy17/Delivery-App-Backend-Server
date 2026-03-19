package com.laptrinhjavaweb.news.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;

import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.request.mongo.*;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPreview;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateRestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface RestaurantService {
    RestaurantDocument createRestaurant(RestaurantInput restaurantInput, String ownerId);

    List<RestaurantDocument> getAllRestaurants(PageRequest pageable);

    Long count();

    UpdateRestaurantResponse updateDeliveryBoundsAndLocation(UpdateDeliveryBoundsInput input);

    RestaurantDocument findById(String id);

    RestaurantDocument getRestaurantDeliveryZoneInfo(String id);

    RestaurantDocument updateTimings(String id, List<TimingsInput> openingTimes);

    RestaurantDocument editRestaurant(RestaurantProfileInput input);

    UpdateRestaurantResponse updateRestaurantBussinessDetails(String id, BussinessDetailsInput input);

    List<RestaurantDocument> getAllRestaurant();

    List<RestaurantDocument> findNearbyRestaurants(double longitude, double latitude, double radiusKm);

    List<RestaurantPreview> findNearByLocation(double longitude, double latitude, String shopType);

    List<RestaurantPreview> getMostOrderedRestaurants(Double latitude, Double longitude);

    List<RestaurantPreview> topRatedVendors(double latitude, double longitude);

    AuthData login(String username, String password);

    RestaurantDocument updateCommission(String id, Double commissionRate);
}
