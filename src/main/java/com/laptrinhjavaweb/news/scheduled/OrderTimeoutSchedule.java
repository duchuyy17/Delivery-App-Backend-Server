package com.laptrinhjavaweb.news.scheduled;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.constant.SystemConstant;
import com.laptrinhjavaweb.news.dto.data.MatchOrderSubscriptionPayload;
import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.publisher.ZoneOrderPublisher;
import com.laptrinhjavaweb.news.repository.OrderRepository;
import com.laptrinhjavaweb.news.repository.RiderRepository;
import com.laptrinhjavaweb.news.service.impl.RiderMatchService;
import com.laptrinhjavaweb.news.service.impl.RiderServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class OrderTimeoutSchedule {
    private final RedisTemplate<String, String> redisTemplate;
    private final RiderMatchService riderMatchService;
    private final OrderRepository orderRepository;
    private final RiderRepository riderRepository;
    private final ZoneOrderPublisher zoneOrderPublisher;
    private final RiderServiceImpl riderServiceImpl;

    @Scheduled(fixedDelay = 5_000)
    public void checkOfferTimeout() {
        System.out.println("checkOfferTimeout");
        long now = System.currentTimeMillis();
        Set<String> expired = redisTemplate.opsForZSet().rangeByScore(SystemConstant.PENDING_OFFER_KEY, 0, now);
        log.info("expried: {}",expired);
        if (expired == null || expired.isEmpty()) return;

        for (String item : expired) {
            redisTemplate.opsForZSet().remove(SystemConstant.PENDING_OFFER_KEY, item);

            String[] parts = item.split(":");
            String orderId = parts[0];
            String riderId = parts[1];

            retryNextRider(orderId, riderId);
        }
    }

    private void retryNextRider(String orderId, String riderId) {
        OrderDocument order = orderRepository.findById(orderId).orElse(null);
        RiderDocument rider = riderRepository.findById(riderId).orElse(null);
        if (order == null || rider == null) {
            return; // hoặc throw exception tùy logic
        }
        riderServiceImpl.clearOffers(riderId);
        pushOrderTimeOutInZone(order, riderId);
        LocationResponse orderLocation = order.getRestaurant().getLocation();
        double orderLat = Double.parseDouble(orderLocation.getCoordinates().getLast());
        double orderLng = Double.parseDouble(orderLocation.getCoordinates().getFirst());

        double[] radiusSteps = {2, 4, 6, 10};

        Optional<String> nextOpt = Optional.empty();

        for (double radius : radiusSteps) {
            nextOpt = riderMatchService.matchNearestRider(orderId, orderLat, orderLng, radius);
            if (nextOpt.isPresent()) break;
        }

        if (nextOpt.isEmpty()) {
            riderMatchService.scheduleOrderRetryAndMarkWaitingRider(orderId, order);
            return;
        }
        riderMatchService.offerToRider(order, nextOpt.get());
    }

    private void pushOrderTimeOutInZone(OrderDocument order, String riderId) {
        MatchOrderSubscriptionPayload payload = new MatchOrderSubscriptionPayload(riderId, "remove", order);
        zoneOrderPublisher.publish(payload);
    }
}
