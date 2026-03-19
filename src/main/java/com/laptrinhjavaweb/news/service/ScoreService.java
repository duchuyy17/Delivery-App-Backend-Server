package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.data.DispatchWeight;
import com.laptrinhjavaweb.news.dto.data.RiderMetrics;

public interface ScoreService {
    double normalizeSpeed(double speed);

    double normalizeTraffic(double traffic);

    double normalizeDistance(double distanceKm, double maxRadiusKm);

    double scoreRider(RiderMetrics m, DispatchWeight w, double maxRadiusKm);
}
