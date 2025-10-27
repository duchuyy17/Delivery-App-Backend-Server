package com.laptrinhjavaweb.news.mongo;

import com.laptrinhjavaweb.news.dto.data.BussinessDetails;
import com.laptrinhjavaweb.news.dto.data.CircleBounds;
import com.laptrinhjavaweb.news.dto.data.DeliveryInfo;
import com.laptrinhjavaweb.news.dto.data.OpeningTimes;
import com.laptrinhjavaweb.news.dto.response.mongo.GeoJsonPolygonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
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
    private String postCode;
    private String plainPassword;
    private String uniqueRestaurantId;
    private String slug;
    private String city;
    private Integer tax;
    @DBRef(lazy = true)
    private OwnerDocument owner;
    private String shopTye;
    private String orderPrefix;
    private String logo;
    private List<String> cuisines = new ArrayList<>();
    private DeliveryInfo deliveryInfo;
    private GeoJsonPolygon deliveryBounds;
    private CircleBounds circleBounds;
    private String boundType;
    private OpeningTimes openingTimes;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    private BussinessDetails bussinessDetails;
    private Float currentWalletAmount;
    private Float totalWalletAmount;
    private Float withdrawnWalletAmount;
    private Boolean stripeDetailsSubmitted;


    public String get_id() {
        return id;
    }
    public String getunique_restaurant_id() {
        return uniqueRestaurantId;
    }
    public Boolean getisAvailable(){
        return isActive;
    }
    public GeoJsonPolygonResponse getdeliveryBounds() {
        if(deliveryBounds == null){
            return null;
        }
        return new GeoJsonPolygonResponse(deliveryBounds);
    }
}
