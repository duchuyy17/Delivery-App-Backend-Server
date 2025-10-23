package com.laptrinhjavaweb.news.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.laptrinhjavaweb.news.dto.AbstractDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class UserResponse extends AbstractDTO {
    String id;
    String userName;
    String password;
    String fullName;
    String email;
    String imgUrl;
    String phone;
    int status;
    LocalDate dateOfBirth;
    String resetPasswordToken;
    List<RoleResponse> roles;
}
