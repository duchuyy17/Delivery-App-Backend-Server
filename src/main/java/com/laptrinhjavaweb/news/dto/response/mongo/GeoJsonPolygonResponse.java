package com.laptrinhjavaweb.news.dto.response.mongo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class GeoJsonPolygonResponse {
    private List<List<List<Double>>> coordinates;

    public GeoJsonPolygonResponse(GeoJsonPolygon polygon) {
        this.coordinates = polygon.getCoordinates().stream()
                .map(lineString -> lineString.getCoordinates().stream()
                        .map(p -> List.of(p.getX(), p.getY()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
