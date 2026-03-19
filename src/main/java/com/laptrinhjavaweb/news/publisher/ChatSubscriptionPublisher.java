package com.laptrinhjavaweb.news.publisher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.mongo.ChatDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class ChatSubscriptionPublisher {
    private final Map<String, Sinks.Many<ChatDocument>> sinks = new ConcurrentHashMap<>();

    public Flux<ChatDocument> subscribe(String orderId) {
        return sinks.computeIfAbsent(orderId, id -> Sinks.many().multicast().onBackpressureBuffer())
                .asFlux();
    }

    public void publish(String orderId, ChatDocument message) {
        Sinks.Many<ChatDocument> sink = sinks.get(orderId);
        if (sink != null) {
            sink.tryEmitNext(message);
        }
    }
}
