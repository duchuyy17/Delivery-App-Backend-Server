package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface ConfigurationRepository extends MongoRepository<ConfigurationDocument, String> {
}
