package com.laptrinhjavaweb.news.service.impl;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.DispatchWeight;
import com.laptrinhjavaweb.news.dto.data.RiderMetrics;
import com.laptrinhjavaweb.news.service.ScoreService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreServiceImpl implements ScoreService {

    private final GoogleMapServiceImpl googleMapService;

    @Override
    public double normalizeDistance(double distanceKm, double maxRadiusKm) {
        // distance = 0  => score = 1
        // distance = maxRadius => score = 0
        return clamp(1 - (distanceKm / maxRadiusKm));
    }

    @Override
    public double normalizeSpeed(double speed) {
        double OPTIMAL_SPEED = 30.0;
        double MAX_DEVIATION = 20.0;

        double deviation = Math.abs(speed - OPTIMAL_SPEED);
        double normalized = 1 - (deviation / MAX_DEVIATION);

        return clamp(normalized);
    }

    @Override
    public double normalizeTraffic(double traffic) {
        double MAX_TRAFFIC = 3.0; // rất kẹt

        double normalized = 1.0 - (traffic - 1.0) / (MAX_TRAFFIC - 1.0);

        return clamp(normalized);
    }

    double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    @Override
    public double scoreRider(RiderMetrics m, DispatchWeight w, double maxRadiusKm) {

        double distanceScore = w.getWDistance() * normalizeDistance(m.getDistanceKm(), maxRadiusKm);

        double acceptanceScore = w.getWAcceptance() * m.getAcceptanceRate();

        double completionScore = w.getWCompletion() * m.getCompletionRate();

        double speedScore = w.getWAvgSpeed() * normalizeSpeed(m.getAvgSpeed());

        double trafficScore = w.getWTraffic() * normalizeTraffic(m.getTraffic());

        double totalScore = distanceScore + acceptanceScore + completionScore + speedScore + trafficScore;

        log.info(
                """
		Rider scoring:
		- riderId        : {}
		- riderName:     : {}
		- distanceKm     : {}, score = {}
		- acceptanceRate : {}, score = {}
		- completionRate : {}, score = {}
		- avgSpeed       : {}, score = {}
		- traffic        : {}, score = {}
		=> TOTAL SCORE   : {}
		""",
                m.getRiderId(),
                m.getRiderName(),
                m.getDistanceKm(),
                distanceScore,
                m.getAcceptanceRate(),
                acceptanceScore,
                m.getCompletionRate(),
                completionScore,
                m.getAvgSpeed(),
                speedScore,
                m.getTraffic(),
                trafficScore,
                totalScore);

        return totalScore;
    }
}
