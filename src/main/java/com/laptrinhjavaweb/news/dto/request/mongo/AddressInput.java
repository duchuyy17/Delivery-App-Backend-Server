package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressInput {
    private String label;
    private String deliveryAddress;
    private String details;
    private String longitude;
    private String latitude;
    private boolean selected;
}
