package com.laptrinhjavaweb.news.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.ChatDocument;

public interface ChatRepository extends MongoRepository<ChatDocument, String> {
    List<ChatDocument> findByOrderIdOrderByCreatedAtAsc(String orderId);
}
