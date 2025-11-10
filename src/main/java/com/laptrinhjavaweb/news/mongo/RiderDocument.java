package com.laptrinhjavaweb.news.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;
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
    private String vehicleType;
    private List<String> assigned; // danh sách ID đơn hàng hoặc khu vực

    @DBRef(lazy = true)
    private ZoneDocument zone; // nested object

    private BussinessDetails bussinessDetails;
    private LicenseDetails licenseDetails;
    private VehicleDetails vehicleDetails;

    public String get_id() {
        return id;
    }
}
