package com.laptrinhjavaweb.news.repository.mongo;

import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends MongoRepository<OrderDocument, String> {
    Page<OrderDocument> findByUser(UserDocument user, Pageable pageable);
    Page<OrderDocument> findByUserOrderByCreatedAtDesc(UserDocument user, Pageable pageable);

    @Query("{ 'restaurant': ?0, 'orderId': { $regex: ?1, $options: 'i' } }")
    List<OrderDocument> searchOrders(String restaurantId, String search);

    Optional<List<OrderDocument>> findByRestaurantId(String restaurantId);

    Optional<List<OrderDocument>> findByRider (RiderDocument rider);

    Optional<List<OrderDocument>> findByRiderIsNull();
}
