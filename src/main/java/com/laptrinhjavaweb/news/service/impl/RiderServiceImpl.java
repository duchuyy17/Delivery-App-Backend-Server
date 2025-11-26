package com.laptrinhjavaweb.news.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.repository.mongo.OrderRepository;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.OrderService;
import com.laptrinhjavaweb.news.service.RiderService;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.RiderMapper;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.repository.mongo.RiderRepository;
import com.laptrinhjavaweb.news.repository.mongo.ZoneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    private final RiderMapper riderMapper;
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    @Override
    public List<RiderDocument> getAllRiders() {
        return riderRepository.findAll();
    }

    @Override
    public RiderDocument createRider(RiderInput input) {
        ZoneDocument zone = null;

        if (input.getZone() != null) {
            zone = zoneRepository
                    .findById(input.getZone())
                    .orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_FOUND));
        }

        RiderDocument rider = RiderDocument.builder()
                .name(input.getName())
                .username(input.getUsername())
                .password(input.getPassword())
                .phone(input.getPhone())
                .available(input.getAvailable() != null ? input.getAvailable() : false)
                .vehicleType(input.getVehicleType())
                .zone(zone)
                .build();

        return riderRepository.save(rider);
    }

    @Override
    public RiderDocument getRiderById(String id) {
        return riderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
    }

    @Override
    public RiderDocument deleteRider(String id) {
        RiderDocument rider =
                riderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        riderRepository.delete(rider);
        return rider;
    }

    @Override
    public RiderDocument editRider(RiderInput input) {
        RiderDocument existing =
                riderRepository.findById(input.getId()).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        if (input.getZone() != null) {
            ZoneDocument zone = zoneRepository
                    .findById(input.getZone())
                    .orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_FOUND));
            existing.setZone(zone);
        }

        riderMapper.updateRider(input, existing);

        return riderRepository.save(existing);
    }

    @Override
    public AuthData login(RiderInput request) {
        RiderDocument rider = riderRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!rider.getPassword().equals(request.getPassword())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        // tạo JWT token
        String token = jwtService.generateToken(rider);

        // cập nhật notification token nếu có
        if (request.getNotificationToken() != null) {
            rider.setNotificationToken(request.getNotificationToken());
            riderRepository.save(rider);
        }

        return AuthData.builder()
                .userId(rider.getId())
                .token(token)
                .build();
    }

    @Override
    public List<OrderDocument> riderOrders(String riderId) {
        RiderDocument riderDocument = riderRepository.findById(riderId)
                .orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        ZoneDocument zone = riderDocument.getZone();
        List<OrderDocument> riderOrders = new ArrayList<>(orderRepository.findByRider(riderDocument)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));
        List<OrderDocument> orders = orderRepository.findByRiderIsNull()
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        // vi tri don hang ph nam trong zone cua tai xe
       orders = orders.stream().filter(order -> {
            LocationResponse location =  order.getRestaurant().getLocation();
            order.setAcceptedAt(new Date());
            GeoJsonPoint inputLocation = new GeoJsonPoint(Double.parseDouble(location.getCoordinates().getFirst()),
                    Double.parseDouble(location.getCoordinates().getLast()));
            GeoJsonPolygon zonePolygon = zone.getLocation2();
            return zonePolygon != null && isPointInsidePolygon(inputLocation, zonePolygon);
        }).toList();

        riderOrders.addAll(orders);
        return riderOrders;
    }

    @Override
    public RiderDocument updateRiderLocation(String latitude, String longitude) throws ParseException {
        String riderId = jwtService.getCurrentUserId();
        RiderDocument rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        GeoJsonPoint loc = new GeoJsonPoint(Double.parseDouble(longitude),Double.parseDouble(latitude));
        rider.setLocation(loc);
        return riderRepository.save(rider);
    }

    private boolean isPointInsidePolygon(GeoJsonPoint point, GeoJsonPolygon polygon) {
        double x = point.getX();
        double y = point.getY();

        List<Point> points = polygon.getPoints();
        boolean inside = false;

        for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            double xi = points.get(i).getX(), yi = points.get(i).getY();
            double xj = points.get(j).getX(), yj = points.get(j).getY();

            boolean intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi + 0.0) + xi);
            if (intersect) inside = !inside;
        }

        return inside;
    }
}
