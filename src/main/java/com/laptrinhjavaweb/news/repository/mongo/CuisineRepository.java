package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;

public interface CuisineRepository extends MongoRepository<CuisineDocument, String> {}
