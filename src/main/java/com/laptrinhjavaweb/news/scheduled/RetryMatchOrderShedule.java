package com.laptrinhjavaweb.news.scheduled;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.constant.OrderStatusConstant;
import com.laptrinhjavaweb.news.dto.data.MatchOrderSubscriptionPayload;
import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.publisher.ZoneOrderPublisher;
import com.laptrinhjavaweb.news.repository.OrderRepository;
import com.laptrinhjavaweb.news.repository.RiderRepository;
import com.laptrinhjavaweb.news.service.impl.RiderMatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.laptrinhjavaweb.news.constant.SystemConstant.*;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RetryMatchOrderShedule {
    private final RedisTemplate<String, String> redisTemplate;
    private final OrderRepository orderRepository;
    private final RiderRepository riderRepository;
    private final RiderMatchService riderMatchService;
    private final ZoneOrderPublisher zoneOrderPublisher;

    @Scheduled(fixedDelay = 5_000)
    public void processDelayQueue() {
        log.info("chua toi lich can xu ly");
        long now = System.currentTimeMillis();

        Set<String> orders = redisTemplate.opsForZSet().rangeByScore(RETRY_KEY, 0, now);
        if (orders == null || orders.isEmpty()) {
            log.debug("[⏳ RETRY_SCHEDULER] No orders to retry yet. now={}", now);
            return;
        }

        log.info("[🔁 RETRY_SCHEDULER] Found {} orders to retry. orderIds={}", orders.size(), orders);
        for (String orderId : orders) {
            // Xóa khỏi delay queue trước
            redisTemplate.opsForZSet().remove(RETRY_KEY, orderId);

            String retryKey = RETRY_COUNT_KEY + orderId;

            // Tăng số lần retry
            Long retryCount = redisTemplate.opsForValue().increment(retryKey);

            if (retryCount == null) continue;

            if (retryCount > MAX_RETRY) {

                // Quá 3 lần thì xóa luôn retry counter
                redisTemplate.delete(retryKey);

                log.warn("Order {} exceeded max retry. Stop retrying.", orderId);
                // cập nhật trạng thái hủy đơn hàng
                OrderDocument order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
                order.setOrderStatus(OrderStatusConstant.CANCELLED);
                orderRepository.save(order);
                continue; // Không gọi retryMatch nữa
            }

            // Set TTL cho retry counter (tránh memory leak)
            redisTemplate.expire(retryKey, 10, TimeUnit.MINUTES);
            retryMatch(orderId);
        }
    }

    public void retryMatch(String orderId) {
        log.info("[🔁 RETRY_MATCH] Start retry for orderId={}", orderId);

        OrderDocument order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            log.warn("[❌ RETRY_MATCH] Order not found, orderId={}", orderId);
            return;
        }

        if (!OrderStatusConstant.WAITINGRIDER.equals(order.getOrderStatus())) {
            log.info(
                    "[⏹ RETRY_MATCH] Order status={}, remove from retry queue, orderId={}",
                    order.getOrderStatus(),
                    orderId);
            redisTemplate.opsForZSet().remove(RETRY_KEY, orderId);
            return;
        }

        LocationResponse orderLocation = order.getRestaurant().getLocation();
        double orderLat = Double.parseDouble(orderLocation.getCoordinates().getLast());
        double orderLng = Double.parseDouble(orderLocation.getCoordinates().getFirst());

        log.debug("[📍 RETRY_MATCH] Order location lat={}, lng={}", orderLat, orderLng);

        double[] radiusSteps = {2, 4, 6, 10};
        Optional<String> riderOpt = Optional.empty();

        for (double radius : radiusSteps) {
            log.info("[🔍 RETRY_MATCH] Try match rider with radius={}km, orderId={}", radius, orderId);

            riderOpt = riderMatchService.matchNearestRider(orderId, orderLat, orderLng, radius);

            if (riderOpt.isPresent()) {
                log.info(
                        "[✅ RETRY_MATCH] Found riderId={} at radius={}km, orderId={}", riderOpt.get(), radius, orderId);
                break;
            } else {
                log.info("[❌ RETRY_MATCH] No rider found at radius={}km, orderId={}", radius, orderId);
            }
        }

        if (riderOpt.isEmpty()) {
            long executeAt = System.currentTimeMillis() + 30_000;

            redisTemplate.opsForZSet().add(RETRY_KEY, orderId, executeAt);

            log.warn(
                    "[⏳ RETRY_MATCH] No rider matched, requeue after 30s, orderId={}, executeAt={}",
                    orderId,
                    executeAt);
            // doi sang util
            return;
        }

        riderOpt.ifPresent(riderId -> {
            riderMatchService.offerToRider(order, riderId);
        });
    }

    private void pushOrderInZone(OrderDocument order, String riderId) {
        MatchOrderSubscriptionPayload payload = new MatchOrderSubscriptionPayload(riderId, "new", order);
        zoneOrderPublisher.publish(payload);
    }
}
