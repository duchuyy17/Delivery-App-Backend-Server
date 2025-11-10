package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("cuisine")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CuisineDocument {
    @Id
    private String id;

    private String name;
    private String description;
    private String image;
    private String shopType;

    private String get_id() {
        return id;
    }
}
