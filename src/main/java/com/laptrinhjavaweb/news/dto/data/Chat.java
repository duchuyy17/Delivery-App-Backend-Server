package com.laptrinhjavaweb.news.dto.data;

import lombok.Data;

import java.util.List;

@Data
public class Chat {
    private String user;
    private String message;
    private List<String> images;
    private Boolean isActive;
}
