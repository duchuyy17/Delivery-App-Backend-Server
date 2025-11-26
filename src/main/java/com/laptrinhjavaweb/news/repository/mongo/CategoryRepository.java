package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<CategoryDocument, String> {
    Optional<List<CategoryDocument>> findByRestaurant(RestaurantDocument restaurantDocument);
}
