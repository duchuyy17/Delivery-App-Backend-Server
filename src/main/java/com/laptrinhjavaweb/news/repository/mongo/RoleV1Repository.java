package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RoleDocument;

public interface RoleV1Repository extends MongoRepository<RoleDocument, String> {}
