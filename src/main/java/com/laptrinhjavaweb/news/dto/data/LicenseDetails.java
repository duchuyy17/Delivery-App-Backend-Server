package com.laptrinhjavaweb.news.dto.data;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseDetails {
    private String number;
    private String expiryDate;
    private String image;
}
