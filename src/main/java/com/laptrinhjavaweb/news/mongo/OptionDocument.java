package com.laptrinhjavaweb.news.mongo;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "options")
public class OptionDocument {
    @Id
    private String id;

    private String title;
    private String description;
    private Decimal128 price;

    public String get_id() {
        return id;
    }
}
