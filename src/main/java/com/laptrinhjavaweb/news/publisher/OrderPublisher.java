// package com.laptrinhjavaweb.news.publisher;
//
// import com.laptrinhjavaweb.news.mongo.OrderDocument;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Sinks;
//
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// @Component
// public class OrderPublisher {
//    private final Map<String, Sinks.Many<OrderDocument>> sinks = new ConcurrentHashMap<>();
//
//    public Flux<OrderDocument> subscribe(String orderId) {
//        return sinks
//                .computeIfAbsent(orderId,
//                        id -> Sinks.many().multicast().onBackpressureBuffer()
//                )
//                .asFlux();
//    }
//
//    public void publish(String orderId, OrderDocument order) {
//        Sinks.Many<OrderDocument> sink = sinks.get(orderId);
//        System.out.println("[PUBLISH] order before: " + order);
//        if (sink != null) {
//            System.out.println("[PUBLISH] order: " + order);
//            sink.tryEmitNext(order);
//        }
//    }
// }
package com.laptrinhjavaweb.news.publisher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.laptrinhjavaweb.news.mongo.OrderDocument;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class OrderPublisher {
    private final Map<String, Sinks.Many<OrderDocument>> sinks = new ConcurrentHashMap<>();

    public Flux<OrderDocument> subscribe(String orderId) {

        Sinks.Many<OrderDocument> sink = sinks.computeIfAbsent(orderId, id -> {
            System.out.println("🆕 Create sink for orderId = " + id);
            return Sinks.many().replay().latest();
        });
        return sink.asFlux().doFinally(signal -> {
            sinks.remove(orderId);
            System.out.println("🧹 Remove sink for orderId = " + orderId);
        });
    }

    public void publish(String orderId, OrderDocument order) {
        Sinks.Many<OrderDocument> sink = sinks.get(orderId);
        System.out.println("[PUBLISH] orderId=" + orderId);

        if (sink != null) {
            sink.tryEmitNext(order);
        }
    }
}
