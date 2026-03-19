package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.TipDocument;

public interface TipRepository extends MongoRepository<TipDocument, String> {}
