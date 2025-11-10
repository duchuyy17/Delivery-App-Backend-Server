package com.laptrinhjavaweb.news.mongo;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.laptrinhjavaweb.news.dto.data.Address;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users") // tương đương @Table(name = "user")
public class UserDocument {
    @Id
    private String id; //
    private String name;
    private String email;
    private String phone;
    private String userName;
    private String password;
    private String fullName;
    private String userType;
    private String status;
    private Date lastLogin;
    private String note;
    private String imgUrl;
    @DBRef(lazy = true)
    private List<AddressDocument> addresses = new ArrayList<>();
    private String resetPasswordToken;
    private boolean phoneIsVerified;
    private boolean emailIsVerified;
    private String appleId;
    private boolean isActive;
    private boolean isOrderNotification;
    private boolean isOfferNotification;
    private Date createdAt;
    private Date updatedAt;
    private String notificationToken;
    private List<String> favourite = new ArrayList<>();
    private String notes;


    public long getCreatedAt() {
        return createdAt != null ? createdAt.getTime() : 0;
    }

    private String get_id() {
        return id;
    }
}
