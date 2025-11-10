package com.laptrinhjavaweb.news.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document(collection = "web_notifications")
@Builder
public class WebNotification {
    @Id
    private String id;

    private String body;
    private String navigateTo;
    private boolean read;
    private Long createdAt;

    private String get_id() {
        return id;
    }
}
