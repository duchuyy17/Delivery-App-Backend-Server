package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class ZoneInput {
    private String id;
    private String title;
    private String description;
    private String isActive;
    private List<List<List<Double>>> coordinates; // [[[x, y], [x, y], ...]]

    public String get_id(){
        return id;
    }
    // getters, setters
}
