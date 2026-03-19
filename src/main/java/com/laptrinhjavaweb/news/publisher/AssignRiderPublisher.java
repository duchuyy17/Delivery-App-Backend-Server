package com.laptrinhjavaweb.news.publisher;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.dto.data.AssignRiderEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class AssignRiderPublisher {
    private final Sinks.Many<AssignRiderEvent> sink;

    public AssignRiderPublisher() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(AssignRiderEvent event) {
        sink.tryEmitNext(event);
    }

    public Flux<AssignRiderEvent> getPublisher() {
        return sink.asFlux();
    }
}
