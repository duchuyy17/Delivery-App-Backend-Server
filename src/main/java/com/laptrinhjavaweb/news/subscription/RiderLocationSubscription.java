package com.laptrinhjavaweb.news.subscription;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.publisher.RiderLocationPublisher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class RiderLocationSubscription {
    private final RiderLocationPublisher publisher;

    @SubscriptionMapping
    public Flux<RiderDocument> subscriptionRiderLocation(@Argument String riderId) {
        return publisher.getPublisher().filter(location -> location.get_id().equals(riderId));
    }

    @SubscriptionMapping
    public Flux<RiderDocument> riderLocationUpdated() {
        System.out.println("ket noi thanh cong nhe!");
        return publisher.getPublisher();
    }
}
