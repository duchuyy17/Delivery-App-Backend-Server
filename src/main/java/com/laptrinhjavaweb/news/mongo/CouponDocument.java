package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("coupon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CouponDocument {
    @Id
    private String id;
    private String title;
    private double discount;
    private boolean enabled;
    private String get_id(){
        return id;
    }
}
