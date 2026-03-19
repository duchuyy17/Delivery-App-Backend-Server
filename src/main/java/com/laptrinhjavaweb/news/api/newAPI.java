package com.laptrinhjavaweb.news.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.news.dto.request.NewDTO;

@RestController
@RequiredArgsConstructor
public class newAPI {
    private final RedisTemplate<String,String> redisTemplate;
    @PostMapping("/new")
    public void test() {
        for (int i = 0; i < 1000; i++) {
            double lat = 20.9 + Math.random() * 0.2;
            double lng = 105.7 + Math.random() * 0.2;

            redisTemplate.opsForGeo()
                    .add("riders:geo", new Point(lng, lat), "rider_" + i);
        }
    }

    @GetMapping("/new")
    public String test1() {
        return "xin chao";
    }
}
