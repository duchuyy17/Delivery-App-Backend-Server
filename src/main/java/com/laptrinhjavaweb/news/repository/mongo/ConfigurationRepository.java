package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;

public interface ConfigurationRepository extends MongoRepository<ConfigurationDocument, String> {}
