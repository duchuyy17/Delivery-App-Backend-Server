package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.dto.data.OpeningTimes;
import com.laptrinhjavaweb.news.dto.data.Point;
import com.laptrinhjavaweb.news.dto.request.mongo.PointInput;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class RestaurantPreview {
    private String _id;
    private String id;
    private Integer orderId;
    private String orderPrefix;
    private String name;
    private String image;
    private String address;
    private String username;
    private String password;
    private Integer deliveryTime;
    private Float minimumOrder;
    private List<String> sections = new ArrayList<>();
    private Float rating;
    private Boolean isActive;
    private Boolean isAvailable;
    private String slug;
    private Boolean stripeDetailsSubmitted;
    private Float commissionRate;
    private Float tax;
    private String notificationToken;
    private Boolean enableNotification;
    private String shopType;
    private List<String> cuisines = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private Integer reviewCount;
    private Float reviewAverage;
    private Point location;
    private List<OpeningTimes> openingTimes = new ArrayList<>();
    private GeoJsonPolygonResponse deliveryBounds;
    public String get_id(){
        return id;
    }

}
