package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("offer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OfferDocument {
    @Id
    private String id;
    private String name;
    private String tag;
    private List<String> restaurants;
    public String get_id(){
        return id;
    }
}
