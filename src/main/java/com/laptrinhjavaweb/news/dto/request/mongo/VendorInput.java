package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class VendorInput {
    private String name;
    private String email;
    private String password;
    private String image;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
