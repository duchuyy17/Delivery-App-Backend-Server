package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.PermissionDocument;

public interface PermissionV1Repository extends MongoRepository<PermissionDocument, String> {}
