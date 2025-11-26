package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

import java.util.Optional;

public interface RiderRepository extends MongoRepository<RiderDocument, String> {
    Optional<RiderDocument> findByUsername(String username);
}
