package com.laptrinhjavaweb.news.mongo;

import java.util.List;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "variation")
public class VariationDocument {
    @Id
    private String id;

    private String title;
    private Decimal128 price;
    private Decimal128 discounted;
    private List<String> addons;
    private boolean isOutOfStock;

    public String get_id() {
        return id;
    }
}
