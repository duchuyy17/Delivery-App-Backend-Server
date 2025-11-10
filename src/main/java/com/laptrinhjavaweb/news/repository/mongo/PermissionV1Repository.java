package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.PermissionDocument;

public interface PermissionV1Repository extends MongoRepository<PermissionDocument, String> {}
