package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.WebNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebNotificationRepository extends MongoRepository<WebNotification, String> {
}
