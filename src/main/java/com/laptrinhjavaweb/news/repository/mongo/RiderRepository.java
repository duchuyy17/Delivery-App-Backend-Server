package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.RiderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RiderRepository extends MongoRepository<RiderDocument, String> {
}
