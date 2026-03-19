package com.laptrinhjavaweb.news.mongo;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.data.Chat;
import com.laptrinhjavaweb.news.dto.data.OrderAddress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDocument {
    @Id
    private String id;

    private String orderId;

    @DBRef(lazy = true)
    private RestaurantDocument restaurant;

    private OrderAddress deliveryAddress;

    @DBRef(lazy = true)
    private List<OrderItemDocument> items;

    @DBRef(lazy = true)
    private UserDocument user;

    @DBRef(lazy = true)
    private RiderDocument rider;

    @DBRef(lazy = true)
    private ReviewDocument review;

    private String paymentMethod;
    private String paymentStatus;
    private Decimal128 paidAmount;
    private Decimal128 orderAmount;
    private Decimal128 marketplaceCommission;
    private Decimal128 deliveryCommission;
    private String orderStatus; // PENDING
    private Date orderDate;
    private Date expectedTime;

    private boolean isPickedUp;
    private Decimal128 tipping; // sua
    private Decimal128 taxationAmount;
    private Decimal128 deliveryCharges;

    private String instructions;
    private String reason;
    private Boolean isActive;

    private Date createdAt;
    private Date completionTime;
    private Date preparationTime;
    private Date acceptedAt;
    private Date cancelledAt;
    private Date assignedAt;
    private Date pickedAt;
    private Date deliveredAt;
    private Boolean isRiderRinged;
    private Chat chat = new Chat();
    private String orderType;
    private double distanceKm;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    public String getcreatedAt() {
        return formatDate(createdAt);
    }

    public String getexpectedTime() {
        return formatDate(expectedTime);
    }

    public String getacceptedAt() {
        return formatDate(acceptedAt);
    }

    public String getorderDate() {
        return formatDate(orderDate);
    }

    public String getcompletionTime() {
        return formatDate(completionTime);
    }

    public String getpreparationTime() {
        return formatDate(preparationTime);
    }

    public Date getPreparationTimeRaw() {
        return this.preparationTime;
    }

    public Date getCompletionTimeRaw() {
        return this.completionTime;
    }

    public String getcancelledAt() {
        return formatDate(cancelledAt);
    }

    public String getassignedAt() {
        return formatDate(assignedAt);
    }

    public String getpickedAt() {
        return formatDate(pickedAt);
    }

    public String getdeliveredAt() {
        return formatDate(deliveredAt);
    }

    // Hàm helper xử lý null + format
    private String formatDate(Date date) {
        if (date == null) return "";
        Instant instant = date.toInstant(); // convert Date sang Instant
        return formatter.format(instant);
    }

    public String get_id() {
        return id;
    }
}
