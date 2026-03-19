package com.laptrinhjavaweb.news.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.data.*;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.repository.*;
import com.laptrinhjavaweb.news.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final RiderRepository riderRepository;
    private final UserV1Repository userV1Repository;
    private final MongoTemplate mongoTemplate;

    @Override
    public DashboardCounts getDashboardUsersByYear(int year) {
        DashboardCounts result = new DashboardCounts();

        // Demo dữ liệu giống API bạn đưa
        result.setUsersCount(List.of(0, 0, 0, 0, 0, 0, 0, 16, 93, 142, 104, 18));
        result.setVendorsCount(List.of(0, 8, 0, 0, 0, 1, 0, 0, 18, 28, 10, 1));
        result.setRestaurantsCount(List.of(1, 7, 0, 2, 3, 1, 1, 0, 17, 37, 25, 4));
        result.setRidersCount(List.of(3, 4, 8, 4, 5, 2, 3, 1, 15, 14, 15, 0));

        PercentageChange percent = new PercentageChange();
        percent.setUsersPercent(100);
        percent.setVendorsPercent(0);
        percent.setRestaurantsPercent(0);
        percent.setRidersPercent(0);

        result.setPercentageChange(percent);

        return result;
    }

    @Override
    public VendorDashboardGrowthDetails getGrowthDetailsByYear(String vendorId, int year) {
        VendorDashboardGrowthDetails result = new VendorDashboardGrowthDetails();

        // 1) Lấy tất cả restaurant thuộc Owner
        List<String> restaurantIds =
                ownerRepository.findById(vendorId).map(OwnerDocument::getRestaurants).orElse(List.of()).stream()
                        .map(RestaurantDocument::getId)
                        .toList();
        List<RestaurantDocument> restaurantDocuments = ownerRepository
                .findById(vendorId)
                .map(OwnerDocument::getRestaurants)
                .orElse(List.of());
        log.info("restaurantIds:{}", restaurantIds);
        // Nếu không có cửa hàng → trả hết 12 tháng = 0
        if (restaurantIds.isEmpty()) {
            result.setTotalRestaurants(empty12MonthList());
            result.setTotalOrders(empty12MonthList());
            result.setTotalSales(empty12MonthListDouble());
            return result;
        }
        result.setTotalRestaurants(countRestaurantsByMonth(restaurantIds, year));
        result.setTotalOrders(countOrdersByMonth(restaurantDocuments, year));
        result.setTotalSales(List.of(3.0, 4.2, 8.2, 4.3, 5.3, 2.3, 3.3, 1.3, 15.3, 14.3, 15.3, 0.3));

        return result;
    }

    @Override
    public RestaurantDashboardDetails getDashboardRestaurantByYear(String restaurantId, int year) {
        RestaurantDashboardDetails result = new RestaurantDashboardDetails();
        result.setOrdersCount(List.of(3.0, 4.2, 8.2, 4.3, 5.3, 2.3, 3.3, 1.3, 15.3, 14.3, 15.3, 0.3));
        result.setSalesAmount(List.of(0, 8, 0, 0, 0, 1, 0, 0, 18, 28, 10, 1));
        return result;
    }

    @Override
    public DashboardUsers getDashboardUsers() {
        long restaurantCount = restaurantRepository.count();
        long riderCount = riderRepository.count();
        long usersCount = userV1Repository.count();
        long vendorsCount = ownerRepository.countByUserType(UserTypeConstant.VENDOR);
        return DashboardUsers.builder()
                .restaurantsCount(restaurantCount)
                .ridersCount(riderCount)
                .usersCount(usersCount)
                .vendorsCount(vendorsCount)
                .build();
    }

    @Override
    public List<DashboardOrdersByType> getDashboardOrdersByType() {
        long total = orderRepository.count();
        long pickup = orderRepository.countByIsPickedUp(true);
        long delivery = orderRepository.countByIsPickedUp(false);
        return List.of(
                new DashboardOrdersByType(total, "Total Orders"),
                new DashboardOrdersByType(pickup, "Pickup Orders"),
                new DashboardOrdersByType(delivery, "Delivery Orders"));
    }

    @Override
    public List<DashboardSalesByType> getDashboardSalesByType() {
        // 1️⃣ Total Sales
        Aggregation totalAgg = newAggregation(group().sum("orderAmount").as("totalSales"));

        Double totalSales = mongoTemplate
                .aggregate(totalAgg, "order", Result.class)
                .getUniqueMappedResult()
                .getValue();

        // 2️⃣ Total Sales Without Delivery (isPickedUp = false)
        Aggregation withoutDeliveryAgg = newAggregation(
                match(Criteria.where("isPickedUp").is(false)),
                group().sum("orderAmount").as("totalWithoutDelivery"));

        Double totalWithoutDelivery = mongoTemplate
                .aggregate(withoutDeliveryAgg, "order", Result.class)
                .getUniqueMappedResult()
                .getValue();

        // 3️⃣ Total Delivery Charges
        Aggregation deliveryAgg = newAggregation(
                match(Criteria.where("isPickedUp").is(true)),
                group().sum("orderAmount").as("totalWithoutDelivery"));

        AggregationResults<Result> totalResult = mongoTemplate.aggregate(deliveryAgg, "order", Result.class);
        Result totalMapped = totalResult.getUniqueMappedResult();
        Double totalDeliveryCharges = totalMapped != null ? totalMapped.getValue() : 0.0;
        return List.of(
                new DashboardSalesByType(totalSales, "Total Sales"),
                new DashboardSalesByType(totalWithoutDelivery, "Total Sales Without Delivery"),
                new DashboardSalesByType(totalDeliveryCharges, "Total Delivery Charges"));
    }

    @Override
    public RestaurantDashboardOrders getDashboardStats(
            String restaurantId, String start, String end, String dateKeyword) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        Query query = new Query();
        query.addCriteria(Criteria.where("restaurant.$id").is(new ObjectId(restaurantId)));

        query.addCriteria(
                Criteria.where("createdAt").gte(startDate.atStartOfDay()).lte(endDate.atTime(LocalTime.MAX)));

        List<OrderDocument> orders = mongoTemplate.find(query, OrderDocument.class);
        int totalOrders = orders.size();
        double totalSales = orders.stream()
                .mapToDouble(order -> order.getOrderAmount().doubleValue())
                .sum();

        int totalCOD = (int)
                orders.stream().filter(o -> "COD".equals(o.getPaymentMethod())).count();

        int totalCard = (int)
                orders.stream().filter(o -> !"COD".equals(o.getPaymentMethod())).count();

        return new RestaurantDashboardOrders(totalOrders, totalSales, totalCOD, totalCard);
    }

    private static class Result {
        private Double totalSales;
        private Double totalWithoutDelivery;
        private Double totalDeliveryCharges;

        public Double getValue() {
            if (totalSales != null) return totalSales;
            if (totalWithoutDelivery != null) return totalWithoutDelivery;
            if (totalDeliveryCharges != null) return totalDeliveryCharges;
            return 0.0;
        }
    }

    private List<Long> empty12MonthList() {
        return Collections.nCopies(12, 0L);
    }

    private List<Double> empty12MonthListDouble() {
        return Collections.nCopies(12, 0.0);
    }

    private List<Long> countRestaurantsByMonth(List<String> restaurantIds, int year) {

        List<Long> months = new ArrayList<>(Collections.nCopies(12, 0L));

        for (int month = 1; month <= 12; month++) {

            Date start = Date.from(LocalDate.of(year, month, 1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());

            Date end = Date.from(
                    LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth())
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault())
                            .toInstant());

            long count = restaurantRepository.countByIdInAndCreatedAtBetween(restaurantIds, start, end);

            months.set(month - 1, count);
        }

        return months;
    }

    private List<Long> countOrdersByMonth(List<RestaurantDocument> restaurantIds, int year) {

        List<Long> months = new ArrayList<>(Collections.nCopies(12, 0L));

        for (int month = 1; month <= 12; month++) {

            Date start = Date.from(LocalDate.of(year, month, 1)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant());

            Date end = Date.from(
                    LocalDate.of(year, month, YearMonth.of(year, month).lengthOfMonth())
                            .atTime(23, 59, 59)
                            .atZone(ZoneId.systemDefault())
                            .toInstant());
            long count = orderRepository.countByRestaurantInAndCreatedAtBetween(restaurantIds, start, end);
            months.set(month - 1, count);
        }

        return months;
    }
}
