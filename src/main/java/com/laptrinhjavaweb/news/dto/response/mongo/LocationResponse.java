package com.laptrinhjavaweb.news.dto.response.mongo;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import java.util.List;

@Getter
@Data
public class LocationResponse {
    private List<String> coordinates;
    public LocationResponse(GeoJsonPoint pointInput) {
        List<Double> points = pointInput.getCoordinates();
        this.coordinates = points.stream().map(Object::toString).toList();
    }
}
