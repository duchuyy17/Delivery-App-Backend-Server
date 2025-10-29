package com.laptrinhjavaweb.news.mongo;

import com.laptrinhjavaweb.news.dto.response.mongo.GeoJsonPolygonResponse;
import lombok.*;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ZoneDocument {
    @Id
    private String id;
    private String title;
    private String description;
    // Dữ liệu tọa độ đa giác (Polygon)

    private GeoJsonPolygon location;

    private Boolean isActive;

    public String get_id() {
        return id;
    }
    // Getter cho GraphQL đọc được
    public GeoJsonPolygonResponse getlocation() {
        if (location == null) {
            return null;
        }
        return new GeoJsonPolygonResponse(location);
    }
    public GeoJsonPolygon getLocation2() {
        return location;
    }

}
