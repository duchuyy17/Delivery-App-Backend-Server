package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.VariationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariationRepository extends MongoRepository<VariationDocument,String> {

}
