package com.laptrinhjavaweb.news.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface CategoryRepository extends MongoRepository<CategoryDocument, String> {
    Optional<List<CategoryDocument>> findByRestaurant(RestaurantDocument restaurantDocument);
}
