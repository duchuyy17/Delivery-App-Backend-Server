package com.laptrinhjavaweb.news.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;

@Entity
@Table(name = "invalidtoken")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidToken {
    @Id
    private String id;

    Date expiryTime;
}
