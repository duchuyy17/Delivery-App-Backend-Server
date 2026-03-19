package com.laptrinhjavaweb.news.graphql;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.dto.response.mongo.OrderPageDTO;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderGraphQLController {
    private final OrderService orderService;
    private final JwtService jwtService;

    @MutationMapping
    public OrderDocument placeOrder(
            @Argument String restaurant,
            @Argument List<OrderInput> orderInput,
            @Argument String paymentMethod,
            @Argument String couponCode,
            @Argument BigDecimal tipping,
            @Argument BigDecimal taxationAmount,
            @Argument AddressInput address,
            @Argument String orderDate,
            @Argument Boolean isPickedUp,
            @Argument BigDecimal deliveryCharges,
            @Argument String instructions)
            throws ParseException {
        return orderService.placeOrder(
                restaurant,
                orderInput,
                paymentMethod,
                couponCode,
                tipping,
                taxationAmount,
                address,
                orderDate,
                isPickedUp,
                deliveryCharges,
                instructions);
    }

    @QueryMapping
    public List<OrderDocument> orders(@Argument Integer offset) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        return orderService.getOrdersByUser(userId, offset);
    }

    @QueryMapping
    public List<OrderDocument> ordersByRestIdWithoutPagination(@Argument String restaurant, @Argument String search) {
        return orderService.getOrders(restaurant, search);
    }

    @MutationMapping
    public OrderDocument acceptOrder(@Argument("_id") String id, @Argument String time) throws ParseException {
        return orderService.acceptOrder(id, time);
    }

    @MutationMapping
    public OrderDocument assignOrder(@Argument String id) throws ParseException {
        String riderId = jwtService.getCurrentUserId();
        return orderService.assignOrder(id, riderId);
    }

    @MutationMapping
    public OrderDocument updateOrderStatusRider(@Argument String id, @Argument String status) throws ParseException {
        return orderService.updateOrderStatusRider(id, status);
    }

    @QueryMapping
    public List<OrderDocument> allOrdersWithoutPagination(
            @Argument String dateKeyword,
            @Argument("starting_date") String startingDate,
            @Argument("ending_date") String endingDate) {
        return orderService.getOrders(dateKeyword, startingDate, endingDate);
    }

    @MutationMapping
    public OrderDocument abortOrder(@Argument String id) {
        return orderService.abortOrder(id);
    }

    @QueryMapping
    public OrderPageDTO getActiveOrders(
            @Argument String restaurantId,
            @Argument Integer page,
            @Argument Integer rowsPerPage,
            @Argument List<String> actions,
            @Argument String search) {
        return orderService.getActiveOrders(restaurantId, page, rowsPerPage, actions, search);
    }

    @MutationMapping
    public OrderDocument assignRider(@Argument String id, @Argument String riderId) {

        return orderService.assignRider(id, riderId);
    }

    @MutationMapping
    public OrderDocument updateStatus(@Argument String id, @Argument String orderStatus) {
        return orderService.updateStatus(id, orderStatus);
    }

    @QueryMapping
    public Double haversineDistance(
            @Argument Double lat1, @Argument Double lng1, @Argument Double lat2, @Argument Double lng2) {
        return orderService.haversine(lat1, lng1, lat2, lng2);
    }
}
