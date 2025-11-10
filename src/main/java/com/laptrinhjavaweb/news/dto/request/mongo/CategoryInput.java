package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryInput {
    private String _id;
    private String id;
    private String restaurant;
    private String title;
    private String image;
    private List<SubCategoryInput> subCategories;
    public String getId(){
        return _id;
    }
}
