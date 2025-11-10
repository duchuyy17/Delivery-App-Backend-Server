package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("sub_category")
public class SubCategoryDocument {
    @Id
    private String id;
    private String title;
    private String parentCategoryId;

    public String get_id(){
        return id;
    }
}
