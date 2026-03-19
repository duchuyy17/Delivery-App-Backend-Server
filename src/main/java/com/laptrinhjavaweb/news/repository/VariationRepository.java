package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.VariationDocument;

public interface VariationRepository extends MongoRepository<VariationDocument, String> {}
