package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.WebNotification;

public interface WebNotificationRepository extends MongoRepository<WebNotification, String> {}
