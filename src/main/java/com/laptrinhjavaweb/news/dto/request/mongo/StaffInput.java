package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.HashSet;


@Data
public class StaffInput {
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
    private HashSet<String> permissions = new HashSet<>();
    private String phone;

}
