package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends MongoRepository<ZoneDocument, String> {
    Optional<List<ZoneDocument>> findByIsActive(Boolean isActive);
}
