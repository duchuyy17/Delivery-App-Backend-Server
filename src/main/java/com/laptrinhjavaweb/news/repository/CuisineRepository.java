package com.laptrinhjavaweb.news.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;

public interface CuisineRepository extends MongoRepository<CuisineDocument, String> {

    // Tìm tất cả Cuisine có name nằm trong danh sách names
    List<CuisineDocument> findAllByNameIn(List<String> names);
}
