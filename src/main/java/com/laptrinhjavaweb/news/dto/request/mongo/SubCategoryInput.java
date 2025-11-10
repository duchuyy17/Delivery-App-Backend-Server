package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class SubCategoryInput {
    private String _id;
    private String id;
    private String title;
    private String parentCategoryId;
    public String getId(){
        return _id;
    }
}
