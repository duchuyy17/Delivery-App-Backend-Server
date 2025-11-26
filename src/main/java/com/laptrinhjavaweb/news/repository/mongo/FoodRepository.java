package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.FoodDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends MongoRepository<FoodDocument, String> {
    Optional<List<FoodDocument>> findByRestaurant(String restaurant);
}
