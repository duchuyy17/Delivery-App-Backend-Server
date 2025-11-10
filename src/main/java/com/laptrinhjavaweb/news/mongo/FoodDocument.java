package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "foods")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDocument {
    @Id
    private String id;
    private String title;
    private String description;
    private String restaurant;
    private String subCategory;
    @DBRef(lazy = true)
    private List<VariationDocument> variations;
    private String image;
    private boolean isActive;
    private boolean isOutOfStock;

    public String get_id(){
        return id;
    }
}
