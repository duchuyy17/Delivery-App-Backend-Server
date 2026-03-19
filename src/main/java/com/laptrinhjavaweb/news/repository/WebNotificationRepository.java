package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.WebNotification;

public interface WebNotificationRepository extends MongoRepository<WebNotification, String> {}
