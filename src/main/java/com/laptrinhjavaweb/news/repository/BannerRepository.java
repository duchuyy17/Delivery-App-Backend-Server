package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.news.mongo.BannerDocument;

@Repository
public interface BannerRepository extends MongoRepository<BannerDocument, String> {}
