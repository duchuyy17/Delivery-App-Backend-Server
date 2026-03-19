package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.AddressDocument;

public interface AddressRepository extends MongoRepository<AddressDocument, String> {}
