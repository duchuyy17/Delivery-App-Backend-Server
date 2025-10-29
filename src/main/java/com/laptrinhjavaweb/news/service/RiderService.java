package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.RiderDocument;

import java.util.List;

public interface RiderService {
    List<RiderDocument> getAllRiders();
    RiderDocument createRider(RiderInput input);
    RiderDocument getRiderById(String id);
    RiderDocument deleteRider(String id);
}
