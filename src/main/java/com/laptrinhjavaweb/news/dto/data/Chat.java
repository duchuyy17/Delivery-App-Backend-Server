package com.laptrinhjavaweb.news.dto.data;

import java.util.List;

import lombok.Data;

@Data
public class Chat {
    private String user;
    private String message;
    private List<String> images;
    private Boolean isActive;
}
