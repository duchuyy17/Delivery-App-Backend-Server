package com.laptrinhjavaweb.news.mongo;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.data.UserInfo;

import lombok.Data;

@Document(collection = "chat")
@Data
public class ChatDocument {
    @Id
    private String id;

    private String orderId;

    private String message;

    private UserInfo user;

    private Date createdAt;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    public String getcreatedAt() {
        return formatDate(createdAt);
    }

    // Hàm helper xử lý null + format
    private String formatDate(Date date) {
        if (date == null) return "";
        Instant instant = date.toInstant(); // convert Date sang Instant
        return formatter.format(instant);
    }
}
