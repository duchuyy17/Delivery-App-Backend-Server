package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

import java.util.List;

public interface RiderService {
    List<RiderDocument> getAllRiders();
}
