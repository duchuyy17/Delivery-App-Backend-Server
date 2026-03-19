package com.laptrinhjavaweb.news.scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.laptrinhjavaweb.news.constant.SystemConstant.GEO_KEY;
import static com.laptrinhjavaweb.news.constant.SystemConstant.RIDER_LAST_UPDATE;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class RemoveRiderOfflineShedule {
    private final RedisTemplate<String,String> redisTemplate;
    @Scheduled(fixedDelay = 5000)
    public void removeOfflineRiders() {

        long timeout = System.currentTimeMillis() - 300000;

        Set<String> offlineRiders = redisTemplate.opsForZSet()
                .rangeByScore(RIDER_LAST_UPDATE, 0, timeout);

        if (offlineRiders == null || offlineRiders.isEmpty()) {
            return;
        }

        for (String riderId : offlineRiders) {

            System.out.println("🚫 Rider offline: " + riderId);

            // xoá khỏi GEO
            redisTemplate.opsForZSet().remove(GEO_KEY, riderId);

            // xoá khỏi lastUpdate
            redisTemplate.opsForZSet().remove(RIDER_LAST_UPDATE, riderId);
        }
    }
}
