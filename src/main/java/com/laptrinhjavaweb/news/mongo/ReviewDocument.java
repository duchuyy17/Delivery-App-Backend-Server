package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDocument {
    @Id
    private String id;

    public String get_id() {
        return id;
    }
}
