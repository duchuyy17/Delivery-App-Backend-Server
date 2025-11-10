package com.laptrinhjavaweb.news.repository.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.news.mongo.UserDocument;


public interface UserV1Repository extends MongoRepository<UserDocument, String> {
    boolean existsByUserName(String userName);

    Optional<UserDocument> findByUserName(String userName);

    Optional<UserDocument> findByEmail(String email);
    Optional<UserDocument> findByPhone(String phone);
}

