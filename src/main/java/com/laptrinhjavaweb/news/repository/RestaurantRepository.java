package com.laptrinhjavaweb.news.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface RestaurantRepository extends MongoRepository<RestaurantDocument, String> {
    List<RestaurantDocument> findByLocationNear(Point point, Distance distance);

    Optional<RestaurantDocument> findByUsername(String username);

    long countByIdInAndCreatedAtBetween(List<String> ids, Date start, Date end);

    List<RestaurantDocument> findByIdIn(List<String> ids);
}
