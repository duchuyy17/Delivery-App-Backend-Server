package com.laptrinhjavaweb.news.entity;

import java.util.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidToken {

    private String id;

    Date expiryTime;
}
