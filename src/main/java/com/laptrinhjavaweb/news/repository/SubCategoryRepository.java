package com.laptrinhjavaweb.news.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;

public interface SubCategoryRepository extends MongoRepository<SubCategoryDocument, String> {
    List<SubCategoryDocument> findByParentCategoryId(String parentCategoryId);
}
