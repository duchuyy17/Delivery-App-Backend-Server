package com.laptrinhjavaweb.news.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.dto.response.mongo.OrderPageDTO;
import com.laptrinhjavaweb.news.mongo.OrderDocument;

public interface OrderService {
    OrderDocument placeOrder(
            String restaurantId,
            List<OrderInput> orderInput,
            String paymentMethod,
            String couponCode,
            BigDecimal tipping,
            BigDecimal taxationAmount,
            AddressInput addressInput,
            String orderDate,
            boolean isPickedUp,
            BigDecimal deliveryBase,
            String instructions)
            throws ParseException;

    List<OrderDocument> getOrdersByUser(String userId, Integer offset);

    List<OrderDocument> getOrders(String restaurantId, String search);

    OrderDocument acceptOrder(String id, String time) throws ParseException;

    OrderDocument assignOrder(String id, String riderId);

    OrderDocument updateOrderStatusRider(String id, String status) throws ParseException;

    List<OrderDocument> getOrders(String dateKeyword, String start, String end);

    OrderDocument abortOrder(String orderId);

    OrderPageDTO getActiveOrders(
            String restaurantId, Integer page, Integer rowsPerPage, List<String> actions, String search);

    OrderDocument assignRider(String orderId, String riderId);

    OrderDocument updateStatus(String id, String orderStatus);

    double haversine(double lat1, double lng1, double lat2, double lng2);
}
