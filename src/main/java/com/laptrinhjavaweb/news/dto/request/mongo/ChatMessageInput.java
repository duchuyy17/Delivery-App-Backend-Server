package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

@Data
public class ChatMessageInput {
    private String message;
    private ChatUserInput user;
}
