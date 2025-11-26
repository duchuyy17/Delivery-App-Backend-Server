package com.laptrinhjavaweb.news.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.data.BussinessDetails;
import com.laptrinhjavaweb.news.dto.data.LicenseDetails;
import com.laptrinhjavaweb.news.dto.data.VehicleDetails;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "riders")
public class RiderDocument {
    @Id
    private String id;

    private String name;
    private String username;
    private String password;
    private String phone;
    private boolean available;
    private boolean isActive;
    private String vehicleType;
    private List<String> assigned = new ArrayList<>(); // danh sách ID đơn hàng hoặc khu vực

    @DBRef(lazy = true)
    private ZoneDocument zone; // nested object

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private BussinessDetails bussinessDetails;
    private LicenseDetails licenseDetails;
    private VehicleDetails vehicleDetails;

    private String notificationToken;
    private String timeZone;

    private Integer currentWalletAmount = 0;

    private Integer totalWalletAmount = 0;

    private Integer withdrawnWalletAmount = 0;

    public String get_id() {
        return id;
    }
}
