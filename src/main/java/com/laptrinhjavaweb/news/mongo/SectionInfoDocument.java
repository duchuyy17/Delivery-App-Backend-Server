package com.laptrinhjavaweb.news.mongo;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("section_info")
public class SectionInfoDocument {
    @Id
    private String id;

    private String name;
    private List<String> restaurants;

    public String get_id() {
        return id;
    }
}
