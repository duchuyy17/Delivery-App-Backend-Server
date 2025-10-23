package com.laptrinhjavaweb.news.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String password;

    private String fullName;

    @Email
    private String email;

    private String imgUrl;
    private int status;
    private String resetPasswordToken;
    private String roleCode;

    LocalDate dateOfBirth;
}
