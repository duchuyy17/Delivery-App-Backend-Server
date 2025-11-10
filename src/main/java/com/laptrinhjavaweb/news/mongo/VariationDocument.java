package com.laptrinhjavaweb.news.mongo;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "variation")
public class VariationDocument {
    @Id
    private String id;
    private String title;
    private Float price;
    private Float discounted;
    private List<String> addons;
    private boolean isOutOfStock;
}
