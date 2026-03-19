package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class TipInput {
    private String id;
    private String _id;
    private List<Integer> tipVariations;
    private Boolean enabled;

    public String getId() {
        return _id;
    }
}
