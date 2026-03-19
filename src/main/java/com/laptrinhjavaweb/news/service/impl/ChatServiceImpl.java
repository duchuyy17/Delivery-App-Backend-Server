package com.laptrinhjavaweb.news.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.UserInfo;
import com.laptrinhjavaweb.news.dto.request.mongo.ChatMessageInput;
import com.laptrinhjavaweb.news.mongo.ChatDocument;
import com.laptrinhjavaweb.news.publisher.ChatSubscriptionPublisher;
import com.laptrinhjavaweb.news.repository.ChatRepository;
import com.laptrinhjavaweb.news.service.ChatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatSubscriptionPublisher publisher;

    @Override
    public List<ChatDocument> getChatByOrder(String orderId) {
        return chatRepository.findByOrderIdOrderByCreatedAtAsc(orderId);
    }

    @Override
    public ChatDocument sendMessage(String orderId, ChatMessageInput input) {
        ChatDocument doc = new ChatDocument();
        doc.setOrderId(orderId);
        doc.setMessage(input.getMessage());
        doc.setUser(new UserInfo(input.getUser().getId(), input.getUser().getName()));
        doc.setCreatedAt(new Date());
        ChatDocument saved = chatRepository.save(doc);
        publisher.publish(orderId, saved);
        return saved;
    }
}
