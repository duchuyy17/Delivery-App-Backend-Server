package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("category")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDocument {
    @Id
    private String id;
    private String title;
    private String image;
    @DBRef(lazy = true)
    private List<SubCategoryDocument> subCategories = new ArrayList<>();
    @DBRef(lazy = true)
    private List<FoodDocument> foods = new ArrayList<>();
    private Date createdAt;
    private Date updatedAt;

    @DBRef(lazy = true)
    private RestaurantDocument restaurant;

    public String get_id(){
        return id;
    }
}
