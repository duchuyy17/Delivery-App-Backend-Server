package com.laptrinhjavaweb.news.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.CouponDocument;

public interface CouponRepository extends MongoRepository<CouponDocument, String> {}
