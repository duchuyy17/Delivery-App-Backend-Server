package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.service.GoogleMapService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GoogleMapGraphQLController {
    private final GoogleMapService googleMapService;

    @QueryMapping
    public Double getTrafficFactor(
            @Argument double lat1, @Argument double lng1, @Argument double lat2, @Argument double lng2) {

        return googleMapService.getTrafficFactor(lat1, lng1, lat2, lng2);
    }

    @QueryMapping
    public long getTravelTimeSeconds(
            @Argument double originLat,
            @Argument double originLng,
            @Argument double destinationLat,
            @Argument double destinationLng) {
        return googleMapService.calculateTravelTimeSeconds(originLat, originLng, destinationLat, destinationLng);
    }
}
