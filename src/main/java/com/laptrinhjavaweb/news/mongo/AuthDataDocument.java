package com.laptrinhjavaweb.news.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auth_data")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthDataDocument {
    @Id
    private String id;
    private String userId;
    private String token;
    private String password;
    private String plainPassword;
    private Long tokenExpiration;
    private String name;
    private String email;
    private String userType;
    private String phone;
    private Boolean isNewUser;
    private Boolean isActive;
    public String get_id(){
        return id;
    }
}
