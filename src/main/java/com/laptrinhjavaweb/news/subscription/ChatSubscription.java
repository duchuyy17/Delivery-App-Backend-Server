package com.laptrinhjavaweb.news.subscription;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.mongo.ChatDocument;
import com.laptrinhjavaweb.news.publisher.ChatSubscriptionPublisher;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class ChatSubscription {
    private final ChatSubscriptionPublisher publisher;

    @SubscriptionMapping
    public Flux<ChatDocument> subscriptionNewMessage(@Argument("order") String orderId) {
        System.out.println("chat subscribe orderId: " + orderId);
        return publisher.subscribe(orderId);
    }
}
