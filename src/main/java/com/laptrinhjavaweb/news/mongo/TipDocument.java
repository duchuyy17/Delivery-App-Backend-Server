package com.laptrinhjavaweb.news.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tip")
public class TipDocument {
    private String id;
    private List<Integer> tipVariations = new ArrayList<>();
    private boolean enabled;

    public String get_id() {
        return id;
    }
}
