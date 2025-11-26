package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("review")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDocument {
    @Id
    private String id;

    public String get_id(){
        return id;
    }
}
