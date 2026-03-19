package com.laptrinhjavaweb.news.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.Decimal128;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.*;
import com.laptrinhjavaweb.news.dto.request.DateFilterInput;
import com.laptrinhjavaweb.news.dto.request.PaginationInput;
import com.laptrinhjavaweb.news.dto.request.SaveEarningRequest;
import com.laptrinhjavaweb.news.dto.response.mongo.EarningsResponse;
import com.laptrinhjavaweb.news.mongo.EarningDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.repository.EarningRepository;
import com.laptrinhjavaweb.news.repository.RestaurantRepository;
import com.laptrinhjavaweb.news.repository.RiderRepository;
import com.laptrinhjavaweb.news.service.EarningService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EarningServiceImpl implements EarningService {
    private final MongoTemplate mongoTemplate;
    private final EarningRepository earningRepository;
    private final RestaurantRepository restaurantRepository;
    private final RiderRepository riderRepository;

    @Override
    public EarningsResponse getEarnings(
            String userId,
            String userType,
            String orderType,
            String paymentMethod,
            String search,
            PaginationInput pagination,
            DateFilterInput dateFilter) {

        List<Criteria> criteriaList = new ArrayList<>();
        Pageable pageable = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize());

        if (orderType != null) {
            criteriaList.add(Criteria.where("orderType").is(orderType));
        }

        if (paymentMethod != null) {
            criteriaList.add(Criteria.where("paymentMethod").is(paymentMethod));
        }

        if (search != null && !search.isBlank()) {
            criteriaList.add(Criteria.where("orderId").regex(search, "i"));
        }

        if (dateFilter.getStarting_date() != null) {
            Instant instant = Instant.parse(dateFilter.getStarting_date());
            criteriaList.add(Criteria.where("createdAt").gte(instant));
        }

        if (dateFilter.getEnding_date() != null) {
            Instant instant = Instant.parse(dateFilter.getEnding_date());
            criteriaList.add(Criteria.where("createdAt").lte(instant));
        }

        Criteria criteria = criteriaList.isEmpty() ? new Criteria() : new Criteria().andOperator(criteriaList);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "createdAt")),
                Aggregation.skip(pagination.getPageNo() - 1),
                Aggregation.limit(pagination.getPageSize()));
        List<EarningDocument> result = mongoTemplate
                .aggregate(aggregation, "earning", EarningDocument.class)
                .getMappedResults();
        Query countQuery = new Query(criteria);
        long total = mongoTemplate.count(countQuery, EarningDocument.class);
        Page<EarningDocument> earningPageResult = new PageImpl<>(result, pageable, total);

        List<EarningDocument> earnings = earningPageResult.getContent();
        GrandTotalEarnings grandTotal = calculateGrandTotal(earnings);
        Pagination paginationResponse = Pagination.builder().total(total).build();

        EarningsData data = new EarningsData();
        data.setEarnings(earnings);
        data.setGrandTotalEarnings(grandTotal);

        return EarningsResponse.builder()
                .message("success")
                .pagination(paginationResponse)
                .data(data)
                .build();
    }

    @Override
    public EarningDocument saveEarning(SaveEarningRequest request) {
        Date now = new Date();

        // ===== Platform Earnings =====
        PlatformEarnings platformEarnings = new PlatformEarnings();
        platformEarnings.setMarketplaceCommission(
                Decimal128.parse(request.getMarketplaceCommission().toString()));
        platformEarnings.setDeliveryCommission(
                Decimal128.parse(request.getDeliveryCommission().toString()));
        platformEarnings.setTax(Decimal128.parse(request.getTax().toString()));

        // total platform
        BigDecimal platformTotal = request.getMarketplaceCommission().add(request.getDeliveryCommission());

        platformEarnings.setTotalEarnings(Decimal128.parse(platformTotal.toString()));
        // ===== Rider Earnings =====
        RiderDocument rider = mongoTemplate.findById(request.getRiderId(), RiderDocument.class);
        RiderEarnings riderEarnings = new RiderEarnings();
        riderEarnings.setRiderId(rider);
        riderEarnings.setDeliveryFee(Decimal128.parse(request.getDeliveryFee().toString()));
        riderEarnings.setTip(Decimal128.parse(request.getTip().toString()));
        // tong tien tai xe
        BigDecimal riderTotal =
                request.getDeliveryFee().add(request.getTip().subtract(request.getDeliveryCommission()));

        riderEarnings.setTotalEarnings(Decimal128.parse(riderTotal.toString()));
        // cong tien vao vi cho rider
        BigDecimal currentRiderWallet = BigDecimal.ZERO;
        BigDecimal totalRiderWalletAmount = BigDecimal.ZERO;
        if (rider.getCurrentWalletAmount() != null) {
            currentRiderWallet = rider.getCurrentWalletAmount().bigDecimalValue();
        }
        if (rider.getTotalWalletAmount() != null) {
            totalRiderWalletAmount = rider.getTotalWalletAmount().bigDecimalValue();
        }
        BigDecimal newRiderWalletAmount = currentRiderWallet.add(riderTotal);
        BigDecimal newRiderTotalWalletAmount = totalRiderWalletAmount.add(riderTotal);
        rider.setCurrentWalletAmount(Decimal128.parse(newRiderWalletAmount.toString()));
        rider.setTotalWalletAmount(Decimal128.parse(newRiderTotalWalletAmount.toString()));
        riderRepository.save(rider);
        // ===== Store Earnings =====
        RestaurantDocument store = mongoTemplate.findById(request.getStoreId(), RestaurantDocument.class);

        StoreEarnings storeEarnings = new StoreEarnings();
        storeEarnings.setStoreId(store);
        storeEarnings.setOrderAmount(Decimal128.parse(request.getOrderAmount().toString()));
        // ví dụ trừ hoa hồng marketplace
        BigDecimal storeTotal = request.getOrderAmount()
                .subtract(request.getMarketplaceCommission())
                .subtract(request.getTax())
                .subtract(request.getDeliveryFee());

        storeEarnings.setTotalEarnings(Decimal128.parse(storeTotal.toString()));
        // cong tien vao vi cho store
        BigDecimal currentStoreWallet = BigDecimal.ZERO;
        BigDecimal totalStoreWalletAmount = BigDecimal.ZERO;
        if (store.getCurrentWalletAmount() != null) {
            currentStoreWallet = store.getCurrentWalletAmount().bigDecimalValue();
        }
        if (store.getTotalWalletAmount() != null) {
            totalStoreWalletAmount = store.getTotalWalletAmount().bigDecimalValue();
        }
        BigDecimal newAmount = currentStoreWallet.add(storeTotal);
        BigDecimal newTotalWalletAmount = totalStoreWalletAmount.add(storeTotal);
        store.setCurrentWalletAmount(Decimal128.parse(newAmount.toString()));
        store.setTotalWalletAmount(Decimal128.parse(newTotalWalletAmount.toString()));
        restaurantRepository.save(store);
        // ===== Build Earning Document =====
        EarningDocument earning = EarningDocument.builder()
                .orderId(request.getOrderId())
                .orderType(request.getOrderType())
                .paymentMethod(request.getPaymentMethod())
                .createdAt(now)
                .updatedAt(now)
                .platformEarnings(platformEarnings)
                .riderEarnings(riderEarnings)
                .storeEarnings(storeEarnings)
                .build();

        return earningRepository.save(earning);
    }

    private GrandTotalEarnings calculateGrandTotal(List<EarningDocument> earnings) {

        BigDecimal platformTotal = BigDecimal.ZERO;
        BigDecimal riderTotal = BigDecimal.ZERO;
        BigDecimal storeTotal = BigDecimal.ZERO;

        for (EarningDocument e : earnings) {
            if (e.getPlatformEarnings() != null) {
                platformTotal = platformTotal.add(
                        e.getPlatformEarnings().getTotalEarnings().bigDecimalValue());
            }
            if (e.getRiderEarnings() != null) {
                riderTotal =
                        riderTotal.add(e.getRiderEarnings().getTotalEarnings().bigDecimalValue());
            }
            if (e.getStoreEarnings() != null) {
                storeTotal =
                        storeTotal.add(e.getStoreEarnings().getTotalEarnings().bigDecimalValue());
            }
        }

        return GrandTotalEarnings.builder()
                .platformTotal(new Decimal128(platformTotal))
                .riderTotal(new Decimal128(riderTotal))
                .storeTotal(new Decimal128(storeTotal))
                .build();
    }
}
