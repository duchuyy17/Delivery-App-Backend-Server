package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CuisineRepository extends MongoRepository<CuisineDocument, String> {
}
