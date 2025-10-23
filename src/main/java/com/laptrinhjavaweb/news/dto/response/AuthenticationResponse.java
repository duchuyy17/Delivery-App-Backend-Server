package com.laptrinhjavaweb.news.dto.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    boolean isSuccessful;
    String token;
    // thong tin nguoi dung
    String fullName;
    String email;
    String phone;
    Set<String> roles;
}
