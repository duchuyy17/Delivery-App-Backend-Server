package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.HashSet;

import lombok.Data;

@Data
public class StaffInput {
    private String _id;
    private String id;
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
    private HashSet<String> permissions = new HashSet<>();
    private String phone;
    private String phoneNumber;

    public String getId() {
        return _id != null ? _id : id;
    }

    public String getPhoneNumber() {
        return phone;
    }
}
