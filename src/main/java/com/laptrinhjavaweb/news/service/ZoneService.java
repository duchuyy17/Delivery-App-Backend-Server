package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.ZoneInput;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;

public interface ZoneService {
    ZoneDocument findById(String id);

    ZoneDocument createZone(ZoneInput zone);

    List<ZoneDocument> findAll();

    ZoneDocument update(String id, ZoneDocument input);

    Boolean delete(String id);

    List<ZoneDocument> findByIsActive(Boolean isActive);
}
