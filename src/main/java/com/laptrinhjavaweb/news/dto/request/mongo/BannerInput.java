package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.Map;

import lombok.Data;

@Data
public class BannerInput {
    private String id;
    private String _id;
    private String title;
    private String description;
    private String action;
    private String file;
    private String screen;
    private Map<String, Object> parameters;

    public String getId() {
        return _id;
    }
}
