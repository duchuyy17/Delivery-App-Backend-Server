package com.laptrinhjavaweb.news.config;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisCheck {
    @Value("${spring.data.redis.host}")
    private String host;

    @PostConstruct
    public void check() {
        System.out.println(">>> SPRING REDIS HOST = " + host);
    }
}
