package com.laptrinhjavaweb.news.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.laptrinhjavaweb.news.mongo.*;

import lombok.Getter;
import lombok.Setter;

public interface OrderRepository extends MongoRepository<OrderDocument, String> {
    Page<OrderDocument> findByUser(UserDocument user, Pageable pageable);

    Page<OrderDocument> findByUserOrderByCreatedAtDesc(UserDocument user, Pageable pageable);

    @Query("{ 'restaurant': ?0, 'orderId': { $regex: ?1, $options: 'i' } }")
    List<OrderDocument> searchOrders(String restaurantId, String search);

    Optional<List<OrderDocument>> findByRestaurantId(String restaurantId);

    Optional<List<OrderDocument>> findByRider(RiderDocument rider);

    Optional<List<OrderDocument>> findByRiderIsNull();

    @Aggregation(
            pipeline = {
                "{ '$match': { 'restaurant.$id': ?0 } }",
                "{ '$lookup': { 'from': 'order_item', 'localField': 'items.$id', 'foreignField': '_id', 'as': 'itemsDetails' } }",
                "{ '$unwind': '$itemsDetails' }",
                "{ '$group': { '_id': '$itemsDetails.food', 'count': { '$sum': 1 } } }",
                "{ '$sort': { 'count': -1 } }"
            })
    List<PopularAggregationResult> countOrdersPerFoodByRestaurant(ObjectId restaurantId);

    @Setter
    @Getter
    public class PopularAggregationResult {
        private String _id; // trực tiếp foodId
        private Long count;

        public PopularAggregationResult() {}
    }

    @Aggregation(
            pipeline = {
                "{ $match: { $expr: { $and: [ " + "{ $gte: [ { $toDate: '$createdAt' }, { $toDate: ?0 } ] }, "
                        + "{ $lte: [ { $toDate: '$createdAt' }, { $toDate: ?1 } ] } "
                        + "]}}}"
            })
    List<OrderDocument> findOrdersByDateRange(String start, String end);

    long countByRestaurantInAndCreatedAtBetween(List<RestaurantDocument> restaurantIds, Date start, Date end);

    Page<OrderDocument> findByIsActiveIsTrueAndOrderStatusIn(List<String> actions, Pageable pageable);

    @Query("""
		{
		isActive: true,
		$or: [
			{ orderId: { $regex: ?0, $options: 'i' } },
		]
		}
	""")
    Page<OrderDocument> findByIsActiveIsTrue(String search, Pageable pageable);

    @Query(
            value = """
	{
		isActive: true,
		$or: [
		{ orderId: { $regex: ?0, $options: 'i' } },
		]
	}
	""",
            count = true)
    long countActiveOrdersWithSearch(String search);

    Integer countByIsActiveIsTrueAndOrderStatusIn(List<String> actions);

    long countByIsPickedUp(boolean isPickup);
}
