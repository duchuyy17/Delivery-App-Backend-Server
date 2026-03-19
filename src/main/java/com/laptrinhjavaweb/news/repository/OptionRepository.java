package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.OptionDocument;

public interface OptionRepository extends MongoRepository<OptionDocument, String> {}
