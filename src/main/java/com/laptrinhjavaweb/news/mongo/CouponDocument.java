package com.laptrinhjavaweb.news.mongo;

import org.bson.types.Decimal128;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("coupon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CouponDocument {
    @Id
    private String id;

    private String title;
    private Decimal128 discount;
    private boolean enabled;

    private String get_id() {
        return id;
    }
}
