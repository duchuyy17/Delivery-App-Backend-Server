package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("rider_stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RiderStatsDocument {
    @Id
    private String riderId;

    private int totalAccepted = 0;
    private int totalCompleted = 0;
    private int totalOffer = 0;
}
