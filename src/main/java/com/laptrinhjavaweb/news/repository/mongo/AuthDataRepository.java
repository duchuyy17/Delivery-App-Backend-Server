package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.AuthDataDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthDataRepository extends MongoRepository<AuthDataDocument, String> {
    Optional<AuthDataDocument> findByEmail(String email);
}
