package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

public interface RiderRepository extends MongoRepository<RiderDocument, String> {}
