package com.laptrinhjavaweb.news.service.impl;

import static com.laptrinhjavaweb.news.constant.OrderStatusConstant.*;
import static com.laptrinhjavaweb.news.constant.SystemConstant.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.bson.types.Decimal128;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.constant.DeliveryConstant;
import com.laptrinhjavaweb.news.constant.OrderStatusConstant;
import com.laptrinhjavaweb.news.constant.SystemConstant;
import com.laptrinhjavaweb.news.dto.data.OrderAddress;
import com.laptrinhjavaweb.news.dto.data.OrderStatusChangedPayload;
import com.laptrinhjavaweb.news.dto.data.Point;
import com.laptrinhjavaweb.news.dto.request.SaveEarningRequest;
import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.dto.request.mongo.OrderInput;
import com.laptrinhjavaweb.news.dto.response.mongo.LocationResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.OrderPageDTO;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.*;
import com.laptrinhjavaweb.news.publisher.OrderPublisher;
import com.laptrinhjavaweb.news.publisher.OrderStatusPublisher;
import com.laptrinhjavaweb.news.repository.*;
import com.laptrinhjavaweb.news.service.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final JwtService jwtService;
    private final FoodRepository foodRepository;
    private final RestaurantService restaurantService;
    private final UserServiceV1 userService;
    private final RiderService riderService;
    private final UserV1Repository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RiderRepository riderRepository;
    private final OrderPublisher publisher;
    private final OrderStatusPublisher orderStatusPublisher;
    private final RiderMatchService riderMatchService;
    private final EarningService earningService;
    private final RedisTemplate<String, String> redisTemplate;
    private final MongoTemplate mongoTemplate;
    private final GoogleMapServiceImpl googleMapServiceImpl;

    // xem la don thuoc zone nao -> push
    @Override
    public OrderDocument placeOrder(
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
            throws ParseException {
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
        List<OrderItemDocument> items = orderInput.stream()
                .map(o -> {
                    FoodDocument food = foodRepository
                            .findById(o.getFood())
                            .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));

                    VariationDocument variation = food.getVariations().stream()
                            .filter(v -> v.getId().equals(o.getVariation()))
                            .findFirst()
                            .orElseThrow(() -> new AppException(ErrorCode.VARIATION_NOT_FOUND));

                    return OrderItemDocument.builder()
                            .id(UUID.randomUUID().toString())
                            .food(food.getId())
                            .title(food.getTitle())
                            .quantity(o.getQuantity())
                            .image(food.getImage())
                            .variation(variation)
                            .addons(List.of()) // tự bổ sung nếu bạn có addons
                            .isActive(true)
                            .createdAt(new Date())
                            .build();
                })
                .toList();
        List<OrderItemDocument> itemsSaved = orderItemRepository.saveAll(items);
        // tinh khoảng cach giao hàng
        double lngRestaurant = Double.parseDouble(
                restaurantDocument.getLocation().getCoordinates().getFirst());
        double latRestaurant = Double.parseDouble(
                restaurantDocument.getLocation().getCoordinates().getLast());
        double lngDestination =
                Double.parseDouble(orderAddress.getLocation().getCoordinates().getFirst());
        double latDestination =
                Double.parseDouble(orderAddress.getLocation().getCoordinates().getLast());
        double distanceKm = haversine(latRestaurant, lngRestaurant, latDestination, lngDestination);
        // 2. Tính phí giao hàng - phí = phí giao hàng cơ bản + (phí phí theo khoảng cách * mật độ giao thông)
        log.info(
                "📍 Calculating distance for Order - Origin: [{}, {}], Dest: [{}, {}]",
                latRestaurant,
                lngRestaurant,
                latDestination,
                lngDestination);

        log.info("📏 Calculated Distance: {} km", String.format("%.2f", distanceKm));

        Double distanceActual = Math.max(0, (distanceKm - DeliveryConstant.FREE_DISTANCE_KM));
        log.info(
                "🚗 Chargeable Distance (after -{}km free): {} km",
                DeliveryConstant.FREE_DISTANCE_KM,
                String.format("%.2f", distanceActual));

        BigDecimal distanceFee = BigDecimal.valueOf(DeliveryConstant.PER_KM_FEE * distanceActual);
        log.info("💵 Raw Distance Fee: {} (Rate: {}/km)", distanceFee, DeliveryConstant.PER_KM_FEE);

        Double trafficFactorValue =
                googleMapServiceImpl.getTrafficFactor(latRestaurant, lngRestaurant, latDestination, lngDestination);

        if (trafficFactorValue == null || trafficFactorValue <= 0) {
            log.warn("⚠️ Traffic factor invalid or null ({}), defaulting to 1.0", trafficFactorValue);
            trafficFactorValue = 1.0;
        } else {
            log.info("🚦 Traffic Factor from Google: {}", trafficFactorValue);
        }

        BigDecimal trafficFactor = BigDecimal.valueOf(trafficFactorValue);
        BigDecimal surgedDistanceFee = distanceFee.multiply(trafficFactor);
        BigDecimal deliveryCharges = deliveryBase.add(surgedDistanceFee).setScale(0, RoundingMode.HALF_UP);

        log.info(
                "✅ Final Delivery Calculation: [Base: {}] + [Surged Fee: {} (Raw: {} * Factor: {})] = Total: {}",
                deliveryBase,
                surgedDistanceFee,
                distanceFee,
                trafficFactor,
                deliveryCharges);
        // 3. Tính tổng tiền
        BigDecimal total = itemsSaved.stream()
                .map(i -> {
                    BigDecimal price = i.getVariation().getPrice().bigDecimalValue();
                    BigDecimal quantity = BigDecimal.valueOf(i.getQuantity());
                    return price.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal marketplaceCommission =
                total.multiply(restaurantDocument.getCommissionRate().bigDecimalValue());
        total = total.add(deliveryCharges).add(taxationAmount).add(tipping);

        BigDecimal deliveryCommission = deliveryCharges.multiply(BigDecimal.valueOf(0.1));
        Instant orderDateInstant = Instant.parse(orderDate);

        Date preparationTime = new Date();

        // expectedTime = preparationTime
        Date expectedTime = preparationTime;

        // restaurant.deliveryTime = phút (vd 15)
        int deliveryMinutes = restaurantDocument.getDeliveryTime();

        // completion = preparationTime + deliveryMinutes
        Instant instant = preparationTime.toInstant();

        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        Date completionTime = Date.from(zonedDateTime.toInstant());

        // 4. Build Order
        OrderDocument order = OrderDocument.builder()
                .orderId(restaurantDocument.getOrderPrefix() + "-" + restaurantDocument.getOrderId())
                .restaurant(restaurantDocument)
                .deliveryAddress(orderAddress)
                .items(itemsSaved)
                .user(userDocument)
                .orderAmount(new Decimal128(total))
                .marketplaceCommission(new Decimal128(marketplaceCommission))
                .deliveryCommission(new Decimal128(deliveryCommission))
                .paymentMethod(paymentMethod)
                .paidAmount(Decimal128.POSITIVE_ZERO)
                .orderStatus(OrderStatusConstant.PENDING)
                .isRiderRinged(false)
                .paymentStatus("PENDING")
                .reason("")
                .orderDate(Date.from(orderDateInstant))
                .expectedTime(expectedTime)
                .preparationTime(preparationTime)
                .completionTime(completionTime)
                .distanceKm(distanceKm)
                .isPickedUp(isPickedUp)
                .tipping(new Decimal128(tipping))
                .taxationAmount(new Decimal128(taxationAmount))
                .deliveryCharges(new Decimal128(deliveryCharges))
                .instructions(instructions)
                .createdAt(new Date())
                .build();
        OrderDocument orderSaved = orderRepository.save(order);
        restaurantDocument.setOrderId(restaurantDocument.getOrderId() + 1);
        restaurantRepository.save(restaurantDocument);
        if (userDocument.getOrders() == null || userDocument.getOrders().isEmpty()) {
            userDocument.setOrders(List.of(orderSaved));
        } else {
            userDocument.getOrders().add(orderSaved);
        }
        userRepository.save(userDocument);
        notifyOrderStatusChanged(userDocument.getId(), orderSaved, "new");
        return orderSaved;
    }

    @Override
    public List<OrderDocument> getOrdersByUser(String userId, Integer offset) {
        if (offset == null) offset = 0;
        UserDocument userDocument = userService.getUserById(userId);
        List<OrderDocument> orderDocuments = orderRepository
                .findByUserOrderByCreatedAtDesc(userDocument, PageRequest.of(offset, 20))
                .getContent();
        return orderDocuments;
    }

    @Override
    public List<OrderDocument> getOrders(String restaurantId, String search) {
        if (search != null && !search.isEmpty()) {
            return orderRepository.searchOrders(restaurantId, search);
        }

        return orderRepository
                .findByRestaurantId(restaurantId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }

    @Override
    public OrderDocument acceptOrder(String id, String time) throws ParseException {
        OrderDocument order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(OrderStatusConstant.ACCEPTED);
        Date preparationTime = order.getPreparationTimeRaw();
        int minutesToAdd = Integer.parseInt(time); // "10" → 10

        Instant newPrepTime = preparationTime.toInstant().plus(minutesToAdd, ChronoUnit.MINUTES);
        Instant completionInstant = new Date().toInstant().plus(minutesToAdd, ChronoUnit.MINUTES);
        Date completionTime = Date.from(completionInstant);
        order.setPreparationTime(Date.from(newPrepTime));
        order.setAcceptedAt(new Date());
        order.setCompletionTime(completionTime);
        OrderDocument orderSaved = orderRepository.save(order);
        publisher.publish(orderSaved.getId(), orderSaved);

        notifyOrderStatusChanged(order.getUser().getId(), orderSaved, "update");
        LocationResponse orderLocation = order.getRestaurant().getLocation();
        double orderLat = Double.parseDouble(orderLocation.getCoordinates().getLast());
        double orderLng = Double.parseDouble(orderLocation.getCoordinates().getFirst());
        // triển khai thuật toán tìm tài xế gần nhất
        double[] radiusSteps = {2, 4, 6, 10};

        Optional<String> riderOpt = Optional.empty();

        for (double radius : radiusSteps) {
            riderOpt = riderMatchService.matchNearestRider(id, orderLat, orderLng, radius);
            if (riderOpt.isPresent()) break;
        }

        if (riderOpt.isEmpty()) {
            riderMatchService.scheduleOrderRetryAndMarkWaitingRider(id, orderSaved);
        }

        riderOpt.ifPresent(riderId -> {
            // rider bị đơn hàng khác lấy
            // retry rider tiếp theo
            if (!riderMatchService.lockRider(riderId)) {
                riderMatchService.scheduleOrderRetryAndMarkWaitingRider(id, orderSaved);
            }

            riderMatchService.offerToRider(orderSaved, riderId);
        });

        return orderSaved;
    }

    @Override
    public OrderDocument assignOrder(String id, String riderId) {
        OrderDocument order =
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (order.getRider() != null) {
            throw new AppException(ErrorCode.ORDER_ALREADY_ASSIGNED);
        }
        RiderDocument rider =
                riderRepository.findById(riderId).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        // bỏ rider đang bận
        Boolean isBusy = redisTemplate.opsForSet().isMember(BUSY_KEY, riderId);

        if (Boolean.TRUE.equals(isBusy)) {
            throw new AppException(ErrorCode.RIDER_IN_DELIVERY);
        }
        // tinh ETA = max(readyTime, riderArrivalAtStore) + travelTimeToCustomer
        LocationResponse orderLocation = order.getRestaurant().getLocation();
        LocationResponse riderLocation = rider.getLocation();
        Point customerLocation = order.getDeliveryAddress().getLocation();
        double orderLat = Double.parseDouble(orderLocation.getCoordinates().getLast());
        double orderLng = Double.parseDouble(orderLocation.getCoordinates().getFirst());
        double riderLat = Double.parseDouble(riderLocation.getCoordinates().getLast());
        double riderLng = Double.parseDouble(riderLocation.getCoordinates().getFirst());
        double customerLat =
                Double.parseDouble(customerLocation.getCoordinates().getLast());
        double customerLng =
                Double.parseDouble(customerLocation.getCoordinates().getFirst());
        long travelTimeToStore =
                googleMapServiceImpl.calculateTravelTimeSeconds(riderLat, riderLng, orderLat, orderLng);
        long travelTimeToCustomer =
                googleMapServiceImpl.calculateTravelTimeSeconds(orderLat, orderLng, customerLat, customerLng);
        log.info(
                "Calculating travel times for Rider to Store: [{}, {}] -> [{}, {}] and Store to Customer: [{}, {}] -> [{}, {}]",
                riderLat,
                riderLng,
                orderLat,
                orderLng,
                orderLat,
                orderLng,
                customerLat,
                customerLng);
        log.info(
                "Travel Time Results - To Store: {}s, To Customer: {}s. Total: {}s",
                travelTimeToStore,
                travelTimeToCustomer,
                (travelTimeToStore + travelTimeToCustomer));
        ZonedDateTime completionUTC = Instant.now().atZone(ZoneId.of("UTC")).plusSeconds(travelTimeToStore);

        Instant estimatedRiderArrival = completionUTC.toInstant();
        Date readyTime = order.getCompletionTimeRaw();
        Instant readyInstant = readyTime.toInstant();
        Instant startInstant = readyInstant.isAfter(estimatedRiderArrival) ? readyInstant : estimatedRiderArrival;
        Instant etaInstant = startInstant.plus(travelTimeToCustomer, ChronoUnit.SECONDS);
        Date eta = Date.from(etaInstant);
        order.setCompletionTime(eta);
        // set trang thai don hang
        order.setOrderStatus(OrderStatusConstant.ASSIGNED);
        order.setAssignedAt(new Date());
        order.setRider(rider);
        OrderDocument orderSaved = orderRepository.save(order);
        publisher.publish(orderSaved.getId(), orderSaved);
        notifyOrderStatusChanged(order.getUser().getId(), orderSaved, "update");
        // thêm vào bảng log event rider
        mongoTemplate.upsert(
                Query.query(Criteria.where("riderId").is(riderId)),
                new Update().inc("totalAccepted", 1),
                RiderStatsDocument.class);
        redisTemplate.opsForZSet().remove(SystemConstant.PENDING_OFFER_KEY, id + ":" + riderId);
        riderService.clearOffers(riderId);
        return orderSaved;
    }

    @Override
    public OrderDocument assignRider(String orderId, String riderId) {
        // 1. Lấy order
        OrderDocument order =
                orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // 2. Lấy rider
        RiderDocument rider =
                riderRepository.findById(riderId).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));

        // bỏ rider đang bận
        Boolean isBusy = redisTemplate.opsForSet().isMember(BUSY_KEY, riderId);

        if (Boolean.TRUE.equals(isBusy)) {
            throw new AppException(ErrorCode.RIDER_IN_DELIVERY);
        }
        order.setRider(rider);
        order.setOrderStatus(OrderStatusConstant.ASSIGNED);
        order.setAssignedAt(new Date());
        OrderDocument orderSaved = orderRepository.save(order);
        publisher.publish(orderSaved.getId(), orderSaved);
        notifyOrderStatusChanged(order.getUser().getId(), orderSaved, "update");

        // thêm vào bảng log event rider
        mongoTemplate.upsert(
                Query.query(Criteria.where("riderId").is(riderId)),
                new Update().inc("totalAccepted", 1),
                RiderStatsDocument.class);
        redisTemplate.opsForZSet().remove(SystemConstant.PENDING_OFFER_KEY, orderId + ":" + riderId);
        riderService.clearOffers(riderId);
        return order;
    }

    @Override
    public OrderDocument updateOrderStatusRider(String id, String status) throws ParseException {

        OrderDocument order =
                orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String currentRiderId = jwtService.getCurrentUserId();
        String current = order.getOrderStatus();
        // Kiểm tra tính hợp lệ của trạng thái
        if (!VALID_TRANSITIONS.getOrDefault(current, List.of()).contains(status)) {
            throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
        }
        // Kiểm tra xem tài xế có phải rider assigned không
        if (!order.getRider().getId().equals(currentRiderId)) {
            throw new AppException(ErrorCode.RIDER_NOT_ASSIGNED);
        }
        if (status.equals(OrderStatusConstant.PICKED)) {
            order.setPickedAt(new Date());
        }
        if (status.equals(OrderStatusConstant.DELIVERED)) {
            order.setDeliveredAt(new Date());
            redisTemplate.opsForSet().remove(BUSY_KEY, currentRiderId);
            mongoTemplate.upsert(
                    Query.query(Criteria.where("riderId").is(currentRiderId)),
                    new Update().inc("totalCompleted", 1),
                    RiderStatsDocument.class);
            earningService.saveEarning(SaveEarningRequest.builder()
                    .orderId(id)
                    .orderType(order.getOrderType() != null ? order.getOrderType() : DELIVERY)
                    .paymentMethod(order.getPaymentMethod())
                    .tax(order.getTaxationAmount().bigDecimalValue())
                    .marketplaceCommission(order.getMarketplaceCommission().bigDecimalValue())
                    .deliveryCommission(order.getDeliveryCommission().bigDecimalValue())
                    .riderId(order.getRider().getId())
                    .deliveryFee(order.getDeliveryCharges().bigDecimalValue())
                    .tip(order.getTipping().bigDecimalValue())
                    .storeId(order.getRestaurant().getId())
                    .orderAmount(order.getOrderAmount().bigDecimalValue())
                    .build());
        }

        order.setOrderStatus(status);
        // gui thong bao firebase cho nha hang, user
        // redis lock

        OrderDocument orderSaved = orderRepository.save(order);
        publisher.publish(orderSaved.getId(), orderSaved);
        notifyOrderStatusChanged(order.getUser().getId(), orderSaved, "update");
        return orderSaved;
    }

    @Override
    public List<OrderDocument> getOrders(String dateKeyword, String startingDate, String endingDate) {
        LocalDate start;
        LocalDate end;

        switch (dateKeyword.toUpperCase()) {
            case "WEEK":
                start = LocalDate.now().with(DayOfWeek.MONDAY);
                end = LocalDate.now().with(DayOfWeek.SUNDAY);
                break;
            case "MONTH":
                start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
                end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
                break;
            case "YEAR":
                start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
                end = LocalDate.of(LocalDate.now().getYear(), 12, 31);
                break;
            case "ALL":
                return orderRepository.findAll();
            default: // custom range
                start = LocalDate.parse(startingDate);
                end = LocalDate.parse(endingDate);
        }
        return orderRepository.findOrdersByDateRange(start.toString(), end.toString());
    }

    @Override
    public OrderDocument abortOrder(String orderId) {
        OrderDocument order =
                orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(OrderStatusConstant.CANCELLED);

        return orderRepository.save(order);
    }

    @Override
    public OrderPageDTO getActiveOrders(
            String restaurantId, Integer page, Integer rowsPerPage, List<String> actions, String search) {
        Pageable pageable = PageRequest.of(page - 1, rowsPerPage, Sort.by(Sort.Direction.DESC, "createdAt"));
        if (actions == null || actions.isEmpty()) {
            Page<OrderDocument> result = orderRepository.findByIsActiveIsTrue(search, pageable);
            long totalCount = orderRepository.countActiveOrdersWithSearch(search);
            return new OrderPageDTO(totalCount, result.getContent());
        }
        Page<OrderDocument> result = orderRepository.findByIsActiveIsTrueAndOrderStatusIn(actions, pageable);

        long totalCount = orderRepository.countByIsActiveIsTrueAndOrderStatusIn(actions);
        return new OrderPageDTO(totalCount, result.getContent());
    }

    @Override
    public OrderDocument updateStatus(String id, String orderStatus) {
        OrderDocument order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        String current = order.getOrderStatus();
        // Kiểm tra tính hợp lệ của trạng thái
        if (!VALID_TRANSITIONS.getOrDefault(current, List.of()).contains(orderStatus)) {
            throw new AppException(ErrorCode.ORDER_STATUS_INVALID);
        }
        order.setOrderStatus(orderStatus);
        applyStatusTimestamp(order, orderStatus);
        OrderDocument orderSaved = orderRepository.save(order);
        publisher.publish(orderSaved.getId(), orderSaved);
        notifyOrderStatusChanged(order.getUser().getId(), orderSaved, "update");
        return orderSaved;
    }

    public void notifyOrderStatusChanged(String userId, OrderDocument order, String origin // "new" | "update"
            ) {
        OrderStatusChangedPayload payload = new OrderStatusChangedPayload(userId, origin, order);

        orderStatusPublisher.publish(userId, payload);
    }

    private void applyStatusTimestamp(OrderDocument order, String status) {
        Date now = new Date();
        switch (status) {
            case ACCEPTED -> order.setAcceptedAt(now);
            case ASSIGNED -> order.setAssignedAt(now);
            case PICKED -> order.setPickedAt(now);
            case DELIVERED -> {
                order.setDeliveredAt(now);
                redisTemplate.opsForSet().remove(BUSY_KEY, order.getRider().getId());
            }
            case CANCELLED -> order.setCancelledAt(now);
        }
    }

    @Override
    public double haversine(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLng / 2)
                        * Math.sin(dLng / 2);

        return 2 * R * Math.asin(Math.sqrt(a));
    }
}
