package com.laptrinhjavaweb.news.service.impl;

import static com.laptrinhjavaweb.news.constant.SystemConstant.GEO_KEY;
import static com.laptrinhjavaweb.news.constant.SystemConstant.RIDER_LAST_UPDATE;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.RiderMapper;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.publisher.ZoneOrderPublisher;
import com.laptrinhjavaweb.news.repository.OrderRepository;
import com.laptrinhjavaweb.news.repository.RiderRepository;
import com.laptrinhjavaweb.news.repository.ZoneRepository;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.RiderService;
import com.laptrinhjavaweb.news.service.RiderStatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    private final RiderMapper riderMapper;
    private final JwtService jwtService;
    private final OrderRepository orderRepository;
    private final ZoneOrderPublisher zoneOrderPublisher;
    private final RedisTemplate<String, String> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final RiderStatService riderStatService;

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
        RiderDocument riderSaved = riderRepository.save(rider);
        riderStatService.saveNewRiderStat(riderSaved.getId());
        return riderSaved;
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
        RiderDocument rider = riderRepository
                .findByUsername(request.getUsername())
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

        return AuthData.builder().userId(rider.getId()).token(token).build();
    }

    @Override
    public List<OrderDocument> riderOrders(String riderId) {
        RiderDocument riderDocument =
                riderRepository.findById(riderId).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        List<OrderDocument> riderOrders = new ArrayList<>(orderRepository
                .findByRider(riderDocument)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)));

        //        ZoneDocument riderZone = riderDocument.getZone();
        //        List<OrderDocument> orders = orderRepository.findByRiderIsNull()
        //                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        //        // vi tri don hang ph nam trong zone cua tai xe
        //       orders = orders.stream().filter(order -> {
        //            LocationResponse location =  order.getRestaurant().getLocation();
        //            order.setAcceptedAt(new Date());
        //            GeoJsonPoint inputLocation = new
        // GeoJsonPoint(Double.parseDouble(location.getCoordinates().getFirst()),
        //                    Double.parseDouble(location.getCoordinates().getLast()));
        //            GeoJsonPolygon zonePolygon = riderZone.getLocation2();
        //            return zonePolygon != null && isPointInsidePolygon(inputLocation, zonePolygon);
        //        }).toList();
        List<String> ids = riderDocument.getAssigned();
        List<OrderDocument> orders = orderRepository.findAllById(ids);

        // Lấy id các order đã có trong riderOrders
        Set<String> existingIds = riderOrders.stream().map(OrderDocument::getId).collect(Collectors.toSet());

        riderOrders.addAll(
                orders.stream().filter(o -> !existingIds.contains(o.getId())).toList());
        return riderOrders;
    }

    @Override
    public RiderDocument updateRiderLocation(String latitude, String longitude) throws ParseException {
        String riderId = jwtService.getCurrentUserId();
        RiderDocument rider =
                riderRepository.findById(riderId).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        GeoJsonPoint loc = new GeoJsonPoint(Double.parseDouble(longitude), Double.parseDouble(latitude));
        rider.setLocation(loc);
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(loc.getX(), loc.getY()), riderId);
        // 2️⃣ lưu timestamp update
        redisTemplate.opsForZSet().add(RIDER_LAST_UPDATE, riderId, System.currentTimeMillis()
        );
        return riderRepository.save(rider);
    }

    @Override
    public double getAvgSpeed(String riderId) {
        Aggregation aggregation = Aggregation.newAggregation(
                // STEP 1: filter
                Aggregation.match(Criteria.where("rider.$id")
                        .is(new ObjectId(riderId))
                        .and("orderStatus").is("DELIVERED")
                        .and("pickedAt").ne(null)
                        .and("deliveredAt").ne(null)
                        .and("distanceKm").ne(null)),

                // STEP 2: calculate time (minutes)
                Aggregation.project()
                        .and("distanceKm")
                        .as("distanceKm")
                        .and(ArithmeticOperators.Divide.valueOf(ArithmeticOperators.Subtract.valueOf("deliveredAt")
                                        .subtract("pickedAt"))
                                .divideBy(60000))
                        .as("deliveryTimeMinutes"),

                // STEP 3: calculate speed (km/h)
                Aggregation.project()
                        .and(ArithmeticOperators.Divide.valueOf("distanceKm")
                                .divideBy(ArithmeticOperators.Divide.valueOf("deliveryTimeMinutes")
                                        .divideBy(60)))
                        .as("speedKmPerHour"),

                // STEP 4: average speed
                Aggregation.group().avg("speedKmPerHour").as("avgSpeed"));

        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "order", Document.class);

        Document doc = result.getUniqueMappedResult();
        return doc != null ? doc.getDouble("avgSpeed") : 0.0;
    }

    @Override
    public void clearOffers(String riderId) {
        RiderDocument rider =
                riderRepository.findById(riderId).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        rider.getAssigned().clear();
        riderRepository.save(rider);
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
