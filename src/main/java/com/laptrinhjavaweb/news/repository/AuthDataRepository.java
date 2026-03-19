package com.laptrinhjavaweb.news.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.AuthDataDocument;

public interface AuthDataRepository extends MongoRepository<AuthDataDocument, String> {
    Optional<AuthDataDocument> findByEmail(String email);
}
