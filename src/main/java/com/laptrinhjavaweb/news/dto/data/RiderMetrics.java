package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiderMetrics {
    String riderId;
    String riderName;
    double distanceKm;
    double acceptanceRate; // 0.0 – 1.0
    double completionRate; // 0.0 – 1.0
    double avgSpeed; // km/h
    double traffic; //
}
