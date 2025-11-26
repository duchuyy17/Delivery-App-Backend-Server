package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "order_item")
public class OrderItemDocument {
    private String id;
    private String food;
    private String title;
    private String description;
    private Integer quantity;
    private String image;
    
    private VariationDocument variation;
    private List<AddonDocument> addons;
    private String specialInstructions;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    public String get_id(){
        return id;
    }
}
