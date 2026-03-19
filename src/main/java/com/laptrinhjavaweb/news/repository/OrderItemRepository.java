package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.OrderItemDocument;

public interface OrderItemRepository extends MongoRepository<OrderItemDocument, String> {}
