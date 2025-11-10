package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class VendorInput {

    private String _id;
    private String id;
    private String name;
    private String email;
    private String password;
    private String image;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public String getId() {
        return _id != null ? _id : id;
    }
}
