package com.laptrinhjavaweb.news.mongo;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.data.PlatformEarnings;
import com.laptrinhjavaweb.news.dto.data.RiderEarnings;
import com.laptrinhjavaweb.news.dto.data.StoreEarnings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("earning")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EarningDocument {
    @Id
    private String id;

    private String orderId;
    private String orderType;
    private String paymentMethod;

    private Date createdAt;
    private Date updatedAt;

    private PlatformEarnings platformEarnings;
    private RiderEarnings riderEarnings;
    private StoreEarnings storeEarnings;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    public String get_id() {
        return id;
    }

    public String getcreatedAt() {
        return formatDate(createdAt);
    }

    public String getupdatedAt() {
        return formatDate(updatedAt);
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        Instant instant = date.toInstant(); // convert Date sang Instant
        return formatter.format(instant);
    }
}
