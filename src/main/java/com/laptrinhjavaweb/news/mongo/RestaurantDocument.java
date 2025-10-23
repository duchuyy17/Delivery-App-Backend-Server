package com.laptrinhjavaweb.news.mongo;

import com.laptrinhjavaweb.news.dto.data.DeliveryInfo;
import com.laptrinhjavaweb.news.dto.data.OpeningTimes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Restaurant")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestaurantDocument {
    @Id
    private String id;
    private String orderId;
    private String name;
    private String image;
    private String address;
    private Integer deliveryTime;
    private Integer minimumOrder;
    private boolean isActive;
    private Integer commissionRate;
    private String username;
    private String password;
    private String plainPassword;
    private String uniqueRestaurantId;
    private String slug;
    private Integer tax;
    @DBRef(lazy = true)
    private OwnerDocument owner;
    private String shopTye;
    private String orderPrefix;
    private String logo;
    private List<String> cuisines = new ArrayList<>();
    private DeliveryInfo deliveryInfo;
    private OpeningTimes openingTimes;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    public String get_id() {
        return id;
    }
    public String getunique_restaurant_id() {
        return uniqueRestaurantId;
    }

}
