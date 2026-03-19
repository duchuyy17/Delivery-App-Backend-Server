package com.laptrinhjavaweb.news.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

public interface RiderRepository extends MongoRepository<RiderDocument, String> {
    Optional<RiderDocument> findByUsername(String username);
}
