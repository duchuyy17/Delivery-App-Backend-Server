package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class RiderInput {
    private String id;
    private String _id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private Boolean available;
    private String vehicleType;
    private String zone;
    private String notificationToken;
    private String timeZone;

    public String getId() {
        return _id != null ? _id : id;
    }
}
