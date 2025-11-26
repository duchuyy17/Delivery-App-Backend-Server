package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.TipDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TipRepository extends MongoRepository<TipDocument,String> {
}
