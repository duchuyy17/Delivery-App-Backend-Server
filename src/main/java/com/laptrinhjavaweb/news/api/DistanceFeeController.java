package com.laptrinhjavaweb.news.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.laptrinhjavaweb.news.dto.request.TrafficRequest;
import com.laptrinhjavaweb.news.service.GoogleMapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DistanceFeeController {
    private final GoogleMapService googleMapService;

    @PostMapping("/traffic-factor")
    public Double getTrafficFactor(@RequestBody TrafficRequest trafficRequest) {
        return googleMapService.getTrafficFactor(
                trafficRequest.getRestaurantLat(),
                trafficRequest.getRestaurantLng(),
                trafficRequest.getCustomerLat(),
                trafficRequest.getCustomerLng());
    }
}
