package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.OwnerDocument;



import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends MongoRepository<OwnerDocument, String> {
    Optional<OwnerDocument> findByEmail(String email);
    List<OwnerDocument> findByUserType(String userType);
}
