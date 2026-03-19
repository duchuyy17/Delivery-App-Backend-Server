package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.ChatDocument;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    private boolean success;
    private String message;
    private ChatDocument data;
}
