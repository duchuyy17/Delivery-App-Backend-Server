package com.laptrinhjavaweb.news.subscription;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.AssignRiderSubscriptionPayload;
import com.laptrinhjavaweb.news.dto.data.MatchOrderSubscriptionPayload;
import com.laptrinhjavaweb.news.dto.data.OrderStatusChangedPayload;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.publisher.AssignRiderPublisher;
import com.laptrinhjavaweb.news.publisher.OrderPublisher;
import com.laptrinhjavaweb.news.publisher.OrderStatusPublisher;
import com.laptrinhjavaweb.news.publisher.ZoneOrderPublisher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class OrderSubscription {
    private final OrderPublisher orderPublisher;
    private final OrderStatusPublisher orderStatusPublisher;
    private final ZoneOrderPublisher zoneOrderPublisher;
    private final AssignRiderPublisher assignRiderPublisher;

    @SubscriptionMapping
    public Flux<OrderDocument> subscriptionOrder(@Argument String id) {
        System.out.println("✅ Client subscribe orderId = " + id);
        return orderPublisher.subscribe(id);
    }

    @SubscriptionMapping
    public Flux<OrderStatusChangedPayload> orderStatusChanged(@Argument String userId) {
        System.out.println("✅ Client subscribe orderStatusChanged userId = " + userId);
        return orderStatusPublisher.subscribe(userId);
    }

    @SubscriptionMapping
    public Flux<MatchOrderSubscriptionPayload> subscriptionMatchOrders(@Argument String riderId) {
        System.out.println("✅ Client subscribe riderId = " + riderId);
        return zoneOrderPublisher.getPublisher().filter(event -> riderId.equals(event.getRiderId()))
                .doOnNext(e -> System.out.println("SUBSCRIBER RECEIVE " + e.getOrder().getId()));
    }

    @SubscriptionMapping
    public Flux<AssignRiderSubscriptionPayload> subscriptionAssignRider(@Argument String riderId) {
        System.out.println("✅ Client subscribed riderId = " + riderId);

        return assignRiderPublisher
                .getPublisher()
                .filter(event -> riderId.equals(event.getRiderId()))
                .map(event -> new AssignRiderSubscriptionPayload(event.getOrder(), event.getOrigin()));
    }
}
