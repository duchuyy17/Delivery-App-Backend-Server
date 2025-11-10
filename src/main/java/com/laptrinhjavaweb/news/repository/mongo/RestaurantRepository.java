package com.laptrinhjavaweb.news.repository.mongo;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import java.util.List;

public interface RestaurantRepository extends MongoRepository<RestaurantDocument, String> {
    List<RestaurantDocument> findByLocationNear(Point point, Distance distance);
}
