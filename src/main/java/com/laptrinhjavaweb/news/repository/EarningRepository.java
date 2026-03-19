package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.EarningDocument;

public interface EarningRepository extends MongoRepository<EarningDocument, String> {}
