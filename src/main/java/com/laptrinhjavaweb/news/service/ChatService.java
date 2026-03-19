package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.ChatMessageInput;
import com.laptrinhjavaweb.news.mongo.ChatDocument;

public interface ChatService {
    List<ChatDocument> getChatByOrder(String orderId);

    ChatDocument sendMessage(String orderId, ChatMessageInput input);
}
