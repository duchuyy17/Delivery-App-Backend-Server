package com.laptrinhjavaweb.news.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.OwnerDocument;

public interface OwnerRepository extends MongoRepository<OwnerDocument, String> {
    Optional<OwnerDocument> findByEmail(String email);

    List<OwnerDocument> findByUserType(String userType);

    Optional<OwnerDocument> findByUserTypeId(String userTypeId);
}
