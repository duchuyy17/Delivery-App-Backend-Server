package com.laptrinhjavaweb.news.mongo;

import java.util.ArrayList;
import java.util.List;

import com.laptrinhjavaweb.news.dto.data.*;
import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.response.mongo.GeoJsonPolygonResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Restaurant")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RestaurantDocument {
    @Id
    private String id;

    private Long orderId;
    private String name;
    private String image;
    private String address;
    private Integer deliveryTime;
    private Integer minimumOrder;
    private boolean isActive;
    private Integer commissionRate;
    private String username;
    private String password;
    private String phone;
    private String postCode;
    private String plainPassword;
    private String uniqueRestaurantId;
    private String slug;
    private String city;
    private Double tax;

    @DBRef(lazy = true)
    private OwnerDocument owner;

    @DBRef(lazy = true)
    private ZoneDocument zone;

    @DBRef(lazy = true)
    private List<CategoryDocument> categories = new ArrayList<>();

    @DBRef(lazy = true)
    private List<OptionDocument> options = new ArrayList<>();

    @DBRef(lazy = true)
    private List<AddonDocument> addons = new ArrayList<>();

    private String shopType;
    private String orderPrefix;
    private String logo;
    private List<String> cuisines = new ArrayList<>();

    private DeliveryInfo deliveryInfo;
    private GeoJsonPolygon deliveryBounds;
    private CircleBounds circleBounds;
    private String boundType;
    private List<String> sections = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List <String> tags = new ArrayList<>();
    private List<OpeningTimes> openingTimes = new ArrayList<>();

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private BussinessDetails bussinessDetails;
    private Float currentWalletAmount;
    private Float totalWalletAmount;
    private Float withdrawnWalletAmount;
    private Boolean stripeDetailsSubmitted;
    private Boolean enableNotification;
    private Boolean isAvailable;

    public String get_id() {
        return id;
    }

    public String getunique_restaurant_id() {
        return uniqueRestaurantId;
    }



    public GeoJsonPolygonResponse getdeliveryBounds() {
        if (deliveryBounds == null) {
            return null;
        }
        return new GeoJsonPolygonResponse(deliveryBounds);
    }
    public LocationResponse getLocation() {
        if (location == null) {
            return null;
        }
        return new LocationResponse(location);
    }


}
