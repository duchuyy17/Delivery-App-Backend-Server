package com.laptrinhjavaweb.news.dto.response.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LocationResponse {
    private List<String> coordinates;

    public LocationResponse(GeoJsonPoint pointInput) {
        List<Double> points = pointInput.getCoordinates();
        this.coordinates = points.stream().map(Object::toString).toList();
    }
}
