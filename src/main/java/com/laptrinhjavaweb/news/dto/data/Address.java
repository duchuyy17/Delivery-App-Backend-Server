package com.laptrinhjavaweb.news.dto.data;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import lombok.Data;

@Data
public class Address {
    private String _id;
    private String id;
    private GeoJsonPoint location;
    private String deliveryAddress;
    private String details;
    private String label;
    private boolean selected;
    public String getId(){
        return _id;
    }
}
