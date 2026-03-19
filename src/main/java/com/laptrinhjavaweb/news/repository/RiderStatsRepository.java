package com.laptrinhjavaweb.news.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.RiderStatsDocument;

public interface RiderStatsRepository extends MongoRepository<RiderStatsDocument, String> {
    Optional<RiderStatsDocument> findByRiderId(String riderId);
}
