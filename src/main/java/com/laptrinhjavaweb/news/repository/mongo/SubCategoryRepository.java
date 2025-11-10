package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubCategoryRepository extends MongoRepository<SubCategoryDocument, String> {
    List<SubCategoryDocument> findByParentCategoryId(String parentCategoryId);
}
