package com.laptrinhjavaweb.news.repository.mongo;


import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RestaurantRepository extends MongoRepository<RestaurantDocument,String> {
}
