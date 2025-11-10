package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class UserInput {
    private String phone;
    private String email;
    private String password;
    private String name;
    private String notificationToken;
    private String appleId;
    private Boolean emailIsVerified;
}
