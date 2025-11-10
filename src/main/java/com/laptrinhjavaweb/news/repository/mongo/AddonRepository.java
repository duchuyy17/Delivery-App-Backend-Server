package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.AddonDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddonRepository extends MongoRepository<AddonDocument,String> {

}
