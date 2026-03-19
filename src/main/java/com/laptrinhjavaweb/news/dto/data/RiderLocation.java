package com.laptrinhjavaweb.news.dto.data;

import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderLocation {
    private String riderId;
    private LocationResponse location;
}
