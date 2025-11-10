package com.laptrinhjavaweb.news.repository.mongo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.laptrinhjavaweb.news.mongo.ZoneDocument;

public interface ZoneRepository extends MongoRepository<ZoneDocument, String> {
    Optional<List<ZoneDocument>> findByIsActive(Boolean isActive);
}
