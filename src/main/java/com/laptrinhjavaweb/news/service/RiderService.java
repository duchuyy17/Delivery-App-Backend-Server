package com.laptrinhjavaweb.news.service;

import java.text.ParseException;
import java.util.List;

import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;

public interface RiderService {
    List<RiderDocument> getAllRiders();

    RiderDocument createRider(RiderInput input);

    RiderDocument getRiderById(String id);

    RiderDocument deleteRider(String id);

    RiderDocument editRider(RiderInput input);

    AuthData login(RiderInput request);

    List<OrderDocument> riderOrders(String riderId);

    RiderDocument updateRiderLocation(String latitude, String longitude) throws ParseException;
}
