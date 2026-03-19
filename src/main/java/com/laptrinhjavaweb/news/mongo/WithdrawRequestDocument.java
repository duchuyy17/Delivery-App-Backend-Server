package com.laptrinhjavaweb.news.mongo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class WithdrawRequestDocument {
    @Id
    private String id;

    private String requestId;
    private Double requestAmount;
    private Date requestTime;
    private String status;
    private Date createdAt;
    private RiderDocument rider;
    private RestaurantDocument store;

    public String getrequestTime() {
        if (requestTime == null) return null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return sdf.format(requestTime);
    }
}
