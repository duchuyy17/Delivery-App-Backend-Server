package com.laptrinhjavaweb.news.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Size;

import com.laptrinhjavaweb.news.validator.DobConstraint;
import com.laptrinhjavaweb.news.validator.PhoneNumberConstraint;
import com.laptrinhjavaweb.news.validator.StrongPassword;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    private String userName;

    @Size(min = 6, max = 20, message = "PASSWORD_INVALID")
    @StrongPassword(message = "PASSWORD_INVALID_FORMAT")
    private String password;

    private String fullName;

    @PhoneNumberConstraint(message = "INVALID_PHONENUMBER")
    private String phone;

    private String email;
    private String imgUrl;
    private int status;
    private String resetPasswordToken;

    @DobConstraint(message = "INVALID_DAYOFBIRTH", min = 2)
    LocalDate dateOfBirth;

    private List<Long> roleCode;
}
