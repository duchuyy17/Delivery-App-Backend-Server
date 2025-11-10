package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.RiderDocument;

public interface RiderService {
    List<RiderDocument> getAllRiders();

    RiderDocument createRider(RiderInput input);

    RiderDocument getRiderById(String id);

    RiderDocument deleteRider(String id);

    RiderDocument editRider(RiderInput input);
}
