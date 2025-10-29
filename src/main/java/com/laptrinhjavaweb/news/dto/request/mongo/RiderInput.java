package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class RiderInput {
    private String name;
    private String username;
    private String password;
    private String phone;
    private Boolean available;
    private String vehicleType;
    private String zone;
}
