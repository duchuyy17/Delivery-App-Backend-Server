package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;

public interface ConfigurationRepository extends MongoRepository<ConfigurationDocument, String> {}
