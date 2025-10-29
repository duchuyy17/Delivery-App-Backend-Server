package com.laptrinhjavaweb.news.dto.data;

import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
public class Address {
    private GeoJsonPoint location;
    private String deliveryAddress;
}
