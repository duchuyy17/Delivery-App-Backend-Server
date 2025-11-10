package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("addon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddonDocument {
    @Id
    private String id;
    private String title;
    private String description;
    private Integer quantityMaximum;
    private Integer quantityMinimum;
    private List<String> options;
    public String get_id(){
        return id;
    }
}
