package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("address")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressDocument {
    @Id
    private String id;
    private String deliveryAddress;
    private String details;
    private String label;
    private boolean selected;
    private GeoJsonPoint location;
    public String get_id(){
        return id;
    }
}
