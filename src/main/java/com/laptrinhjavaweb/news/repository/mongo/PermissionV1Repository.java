package com.laptrinhjavaweb.news.repository.mongo;


import com.laptrinhjavaweb.news.mongo.PermissionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissionV1Repository extends MongoRepository<PermissionDocument, String> {
}
