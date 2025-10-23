package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.entity.UserEntity;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserV1Repository extends MongoRepository<UserDocument, String> {
    boolean existsByUserName(String userName);
    Optional<UserDocument> findByUserName(String userName);
}
