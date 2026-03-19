package com.laptrinhjavaweb.news.publisher;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.mongo.RiderDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class RiderLocationPublisher {
    private final Sinks.Many<RiderDocument> sink = Sinks.many().replay().limit(1); // ✅ FIX CHỖ NÀY

    public void publish(RiderDocument location) {
        System.out.println("[PUBLISH] Rider location: " + location);
        sink.tryEmitNext(location);
    }

    public Flux<RiderDocument> getPublisher() {
        return sink.asFlux();
    }
}
