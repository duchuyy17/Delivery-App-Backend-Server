package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.constant.OrderStatusConstant;
import com.laptrinhjavaweb.news.dto.data.OrderAddress;
import com.laptrinhjavaweb.news.dto.data.Point;
import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.*;
import com.laptrinhjavaweb.news.repository.mongo.*;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.OrderService;
import com.laptrinhjavaweb.news.service.RestaurantService;
import com.laptrinhjavaweb.news.service.UserServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtService jwtService;
    private final FoodRepository foodRepository;
    private final RestaurantService restaurantService;
    private final UserServiceV1 userService;
    private final UserV1Repository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public OrderDocument placeOrder(String restaurantId,
                                    List<OrderInput> orderInput,
                                    String paymentMethod,
                                    String couponCode,
                                    Double tipping,
                                    Double taxationAmount,
                                    AddressInput addressInput,
                                    String orderDate,
                                    boolean isPickedUp,
                                    Double deliveryCharges,
                                    String instructions) throws ParseException {
        String userId = jwtService.getCurrentUserId();
        UserDocument userDocument = userService.getUserById(userId);
        RestaurantDocument restaurantDocument = restaurantService.findById(restaurantId);
        OrderAddress orderAddress = OrderAddress.builder()
                .id(UUID.randomUUID().toString())
                .deliveryAddress(addressInput.getDeliveryAddress())
                .label(addressInput.getLabel())
                .details(addressInput.getDetails())
                .location(Point.builder()
                        .coordinates(List.of(addressInput.getLongitude(), addressInput.getLatitude()))
                        .build())
                .build();
        List<OrderItemDocument> items = orderInput.stream().map(o -> {
            FoodDocument food = foodRepository.findById(o.getFood())
                    .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

            VariationDocument variation = food.getVariations().stream()
                    .filter(v -> v.getId().equals(o.getVariation()))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.VARIATION_NOT_FOUND));

            return  OrderItemDocument.builder()
                    .id(UUID.randomUUID().toString())
                    .food(food.getId())
                    .title(food.getTitle())
                    .quantity(o.getQuantity())
                    .image(food.getImage())
                    .variation(variation)
                    .addons(List.of())  // tự bổ sung nếu bạn có addons
                    .isActive(true)
                    .createdAt(new Date())
                    .build();
        }).toList();
        List<OrderItemDocument> itemsSaved =  orderItemRepository.saveAll(items);
        // 3. Tính tổng tiền
        double orderAmount = itemsSaved.stream()
                .mapToDouble(i -> i.getVariation().getPrice() * i.getQuantity())
                .sum();

        orderAmount += deliveryCharges + tipping + taxationAmount;


        Instant orderDateInstant = Instant.parse(orderDate);

        Date preparationTime = new Date();

        // expectedTime = preparationTime
        Date expectedTime = preparationTime;

        // restaurant.deliveryTime = phút (vd 15)
        int deliveryMinutes = restaurantDocument.getDeliveryTime();

        // completion = preparationTime + deliveryMinutes
        Date completionTime = new Date(preparationTime.getTime() + (long) deliveryMinutes * 60 * 1000);

        // 4. Build Order
        OrderDocument order = OrderDocument.builder()
                .orderId(restaurantDocument.getOrderPrefix()+"-"+restaurantDocument.getOrderId())
                .restaurant(restaurantDocument)
                .deliveryAddress(orderAddress)
                .items(itemsSaved)
                .user(userDocument)
                .orderAmount(orderAmount)
                .paymentMethod(paymentMethod)
                .paidAmount(0.0)
                .orderStatus(OrderStatusConstant.PENDING)
                .isRiderRinged(false)
                .paymentStatus("PENDING")
                .reason("")
                .orderDate(Date.from(orderDateInstant))
                .expectedTime(expectedTime)
                .preparationTime(preparationTime)
                .completionTime(completionTime)
                .isPickedUp(isPickedUp)
                .tipping(tipping)
                .taxationAmount(taxationAmount)
                .deliveryCharges(deliveryCharges)
                .instructions(instructions)
                .createdAt(new Date())
                .build();
        OrderDocument orderSaved =  orderRepository.save(order);
        restaurantDocument.setOrderId(restaurantDocument.getOrderId()+1);
        restaurantRepository.save(restaurantDocument);
        if (userDocument.getOrders() == null || userDocument.getOrders().isEmpty()) {
            userDocument.setOrders(List.of(orderSaved));
        } else {
            userDocument.getOrders().add(orderSaved);
        }
        userRepository.save(userDocument);
        return orderSaved;
    }

    @Override
    public List<OrderDocument> getOrdersByUser(String userId, Integer offset) {
        if (offset == null) offset = 0;
        UserDocument userDocument = userService.getUserById(userId);
        List<OrderDocument> orderDocuments = orderRepository.findByUserOrderByCreatedAtDesc(userDocument, PageRequest.of(offset, 20))
                .getContent();
        return orderDocuments;
    }

    @Override
    public List<OrderDocument> getOrders(String restaurantId, String search) {
        if (search != null && !search.isEmpty()) {
            return orderRepository.searchOrders(restaurantId, search);
        }

        return orderRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }

    @Override
    public OrderDocument acceptOrder(String id, String time) {
        OrderDocument order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatusConstant.ACCEPTED);
        Date preparationTime = order.getPreparationTimeRaw();
        int minutesToAdd = Integer.parseInt(time);             // "10" → 10

        Instant newPrepTime = preparationTime.toInstant()
                .plus(minutesToAdd, ChronoUnit.MINUTES);

        order.setPreparationTime(Date.from(newPrepTime));
        order.setAcceptedAt(new Date());
        return orderRepository.save(order);
    }
}
