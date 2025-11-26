package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.OrderItemDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderItemRepository extends MongoRepository<OrderItemDocument,String> {
}
