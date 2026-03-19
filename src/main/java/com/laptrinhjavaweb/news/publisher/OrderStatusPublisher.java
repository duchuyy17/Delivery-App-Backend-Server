package com.laptrinhjavaweb.news.publisher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.dto.data.OrderStatusChangedPayload;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class OrderStatusPublisher {
    private final Map<String, Sinks.Many<OrderStatusChangedPayload>> sinks = new ConcurrentHashMap<>();

    public Flux<OrderStatusChangedPayload> subscribe(String userId) {
        Sinks.Many<OrderStatusChangedPayload> sink =
                sinks.computeIfAbsent(userId, id -> Sinks.many().replay().latest());

        return sink.asFlux().doFinally(signal -> {
            // cleanup khi user disconnect
            sinks.remove(userId);
        });
    }

    public void publish(String userId, OrderStatusChangedPayload payload) {
        Sinks.Many<OrderStatusChangedPayload> sink = sinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(payload);
        }
    }
}
