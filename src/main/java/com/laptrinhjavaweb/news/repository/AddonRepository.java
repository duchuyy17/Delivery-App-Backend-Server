package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.AddonDocument;

public interface AddonRepository extends MongoRepository<AddonDocument, String> {}
