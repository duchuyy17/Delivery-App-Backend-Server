package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.mongo.OrderDocument;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    OrderDocument placeOrder(String restaurantId,
                             List<OrderInput> orderInput,
                             String paymentMethod,
                             String couponCode,
                             Double tipping,
                             Double taxationAmount,
                             AddressInput addressInput,
                             String orderDate,
                             boolean isPickedUp,
                             Double deliveryCharges,
                             String instructions) throws ParseException;

    List<OrderDocument> getOrdersByUser(String userId, Integer offset);
    List<OrderDocument> getOrders(String restaurantId, String search);
    OrderDocument acceptOrder(String id, String time);
}
