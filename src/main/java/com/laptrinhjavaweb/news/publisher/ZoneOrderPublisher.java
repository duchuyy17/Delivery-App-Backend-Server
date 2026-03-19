package com.laptrinhjavaweb.news.publisher;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.dto.data.MatchOrderSubscriptionPayload;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class ZoneOrderPublisher {
    private final Sinks.Many<MatchOrderSubscriptionPayload> sink;

    public ZoneOrderPublisher() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(MatchOrderSubscriptionPayload payload) {
        System.out.println("PUBLISH ORDER MATCH");
        sink.tryEmitNext(payload);
    }

    public Flux<MatchOrderSubscriptionPayload> getPublisher() {
        return sink.asFlux();
    }
}
