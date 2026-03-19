package com.laptrinhjavaweb.news.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.FoodDocument;

public interface FoodRepository extends MongoRepository<FoodDocument, String> {
    Optional<List<FoodDocument>> findByRestaurant(String restaurant);
}
