package com.laptrinhjavaweb.news.service;

public interface GoogleMapService {
    Double getTrafficFactor(double lat1, double lng1, double lat2, double lng2);

    long calculateTravelTimeSeconds(double originLat, double originLng, double destLat, double destLng);
}
