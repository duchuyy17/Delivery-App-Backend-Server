package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderGraphQLController {
    private final OrderService orderService;
    private  final JwtService jwtService;

    @MutationMapping
    public OrderDocument placeOrder(
            @Argument String restaurant,
            @Argument List<OrderInput> orderInput,
            @Argument String paymentMethod,
            @Argument String couponCode,
            @Argument Double tipping,
            @Argument Double taxationAmount,
            @Argument AddressInput address,
            @Argument String orderDate,
            @Argument Boolean isPickedUp,
            @Argument Double deliveryCharges,
            @Argument String instructions
    ) throws ParseException {
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
                instructions
        );
    }

    @QueryMapping
    public List<OrderDocument> orders(@Argument Integer offset) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        return orderService.getOrdersByUser(userId,offset);
    }

    @QueryMapping
    public List<OrderDocument> ordersByRestIdWithoutPagination(
            @Argument String restaurant,
            @Argument String search
    ) {
        return orderService.getOrders(restaurant, search);
    }
    @MutationMapping
    public OrderDocument acceptOrder(@Argument("_id") String id,@Argument String time){
        return orderService.acceptOrder(id, time);
    }

}
