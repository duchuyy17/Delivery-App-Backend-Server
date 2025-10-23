package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.RoleDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleV1Repository extends MongoRepository<RoleDocument, String> {

}
