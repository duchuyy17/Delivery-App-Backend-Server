package com.laptrinhjavaweb.news.service.impl;

import static com.laptrinhjavaweb.news.constant.SystemConstant.*;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.constant.OrderStatusConstant;
import com.laptrinhjavaweb.news.dto.data.DispatchWeight;
import com.laptrinhjavaweb.news.dto.data.MatchOrderSubscriptionPayload;
import com.laptrinhjavaweb.news.dto.data.RiderMetrics;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.RiderStatsDocument;
import com.laptrinhjavaweb.news.publisher.ZoneOrderPublisher;
import com.laptrinhjavaweb.news.repository.OrderRepository;
import com.laptrinhjavaweb.news.repository.RiderRepository;
import com.laptrinhjavaweb.news.repository.RiderStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderMatchService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RiderRepository riderRepository;
    private final OrderRepository orderRepository;
    private final ZoneOrderPublisher zoneOrderPublisher;
    private final RiderStatsRepository riderStatsRepository;
    private final MongoTemplate mongoTemplate;
    private final ScoreServiceImpl scoreServiceImpl;
    private final RiderServiceImpl riderServiceImpl;
    private final GoogleMapServiceImpl googleMapServiceImpl;

    // lấy 1 danh sách rider gần nhất trong phạm vi radiusKm
    public Optional<String> matchNearestRider(String orderId, double orderLat, double orderLng, double radiusKm) {
        GeoReference<String> reference = GeoReference.fromCoordinate(orderLng, orderLat);

        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
        GeoResults<RedisGeoCommands.GeoLocation<String>> results;
        // dự phòng khi redis down -> đưa vào hàng đợi
        try {
            results = redisTemplate
                    .opsForGeo()
                    .search(
                            GEO_KEY,
                            reference,
                            radius,
                            RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs()
                                    .includeDistance()
                                    .sortAscending()
                                    .limit(10));
        } catch (Exception e) {
            log.error("Redis GEO search failed for order {}", orderId, e);
            return Optional.empty();
        }
        if (results == null || results.getContent().isEmpty()) {
            return Optional.empty();
        }
        // tim list ung cu vien rider
        List<RiderMetrics> candidates = new ArrayList<>();
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> r : results) {
            String riderId = r.getContent().getName();
            // bỏ rider đang bận
            Boolean isBusy = redisTemplate.opsForSet().isMember(BUSY_KEY, riderId);
            if (Boolean.TRUE.equals(isBusy)) continue;

            // bỏ rider đã được offer rồi
            Boolean alreadyOffered = redisTemplate.opsForSet().isMember(ORDER_DISPATCH_KEY + orderId, riderId);
            if (Boolean.TRUE.equals(alreadyOffered)) continue;
            RiderMetrics candidate = new RiderMetrics();
            candidate.setRiderId(riderId);
            candidate.setDistanceKm(r.getDistance().getValue());
            candidates.add(candidate);
        }
        if (candidates.isEmpty()) return Optional.empty();
        // lay top 5 rider gan nhat
        candidates.sort(Comparator.comparingDouble(RiderMetrics::getDistanceKm));
        int topN = Math.min(5, candidates.size());
        List<RiderMetrics> topCandidates = candidates.subList(0, topN);
        // xet da tieu chi tu danh sach rider
        Map<String, Double> scores = new HashMap<>();
        for (RiderMetrics c : topCandidates) {
            RiderStatsDocument riderStatsDocument =
                    riderStatsRepository.findByRiderId(c.getRiderId()).orElseGet(RiderStatsDocument::new);
            RiderDocument riderDocument =
                    riderRepository.findById(c.getRiderId()).orElse(null);
            if (riderDocument == null) return Optional.empty();
            double acceptanceRate = riderStatsDocument.getTotalOffer() == 0
                    ? 0.0
                    : (double) riderStatsDocument.getTotalAccepted() / riderStatsDocument.getTotalOffer();

            double completionRate = riderStatsDocument.getTotalAccepted() == 0
                    ? 0.0
                    : (double) riderStatsDocument.getTotalCompleted() / riderStatsDocument.getTotalAccepted();
            double avgSpeed = riderServiceImpl.getAvgSpeed(c.getRiderId());

            // tinh khoang cach voi rider
            double distanceKm = c.getDistanceKm();
            double riderLng = Double.parseDouble(
                    riderDocument.getLocation().getCoordinates().getFirst());
            double riderLat = Double.parseDouble(
                    riderDocument.getLocation().getCoordinates().getLast());
            double traffic = googleMapServiceImpl.getTrafficFactor(riderLat, riderLng, orderLat, orderLng);
            c = RiderMetrics.builder()
                    .riderId(c.getRiderId())
                    .riderName(riderDocument.getName())
                    .acceptanceRate(acceptanceRate)
                    .completionRate(completionRate)
                    .avgSpeed(avgSpeed)
                    .distanceKm(distanceKm)
                    .traffic(traffic)
                    .build();
            double score = scoreServiceImpl.scoreRider(c, new DispatchWeight(), 10);
            scores.put(c.getRiderId(), score);
        }
        Optional<Map.Entry<String, Double>> bestRider =
                scores.entrySet().stream().max(Map.Entry.comparingByValue());
        System.out.println("riders Score:" + scores);
        String riderId = bestRider.map(Map.Entry::getKey).orElse(null);
        // return riderId
        if (riderId != null) {
            return Optional.of(riderId);
        }

        return Optional.empty();
    }

    public boolean lockRider(String riderId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().add(BUSY_KEY, riderId) == 1);
    }

    public void scheduleOrderRetryAndMarkWaitingRider(String orderId, OrderDocument order) {
        long executeAt = System.currentTimeMillis() + 30_000;

        redisTemplate.opsForZSet().add(RETRY_KEY, orderId, executeAt);

        order.setOrderStatus(OrderStatusConstant.WAITINGRIDER);
        orderRepository.save(order);
    }

    public void offerToRider(OrderDocument order, String riderId) {
        String orderId = order.getId();
        RiderDocument riderDocument = riderRepository.findById(riderId).orElse(null);
        List<String> orderIds = new ArrayList<>(List.of(orderId));
        if (riderDocument.getAssigned() == null || riderDocument.getAssigned().isEmpty()) {
            riderDocument.setAssigned(orderIds);
        } else {
            riderDocument.getAssigned().addAll(orderIds);
        }
        riderRepository.save(riderDocument);

        // mark rider đã được offer
        redisTemplate.opsForSet().add(ORDER_DISPATCH_KEY + orderId, riderId);
        // luu log so lan rider duoc offer
        mongoTemplate.upsert(
                Query.query(Criteria.where("riderId").is(riderId)),
                new Update().inc("totalOffer", 1),
                RiderStatsDocument.class);

        long expireAt = System.currentTimeMillis() + 100_000; // 100s cho rider accept

        // add vào pending_offer để kiểm tra timeout
        redisTemplate.opsForZSet().add(PENDING_OFFER_KEY, orderId + ":" + riderId, expireAt);

        // gửi notify cho rider (pub/sub)
        pushOrderInZone(order, riderId);

        orderRepository.save(order);
    }

    private void pushOrderInZone(OrderDocument order, String riderId) {
        MatchOrderSubscriptionPayload payload = new MatchOrderSubscriptionPayload(riderId, "new", order);
        zoneOrderPublisher.publish(payload);
    }
}
