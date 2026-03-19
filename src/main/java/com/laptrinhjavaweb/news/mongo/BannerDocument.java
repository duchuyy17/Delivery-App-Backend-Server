package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("banner")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BannerDocument {
    @Id
    private String id;

    private String title;
    private String description;
    private String action;
    private String screen;
    private String file;
    private Object parameters; // kiểu JSON → dùng Object

    private String get_id() {
        return id;
    }
}
