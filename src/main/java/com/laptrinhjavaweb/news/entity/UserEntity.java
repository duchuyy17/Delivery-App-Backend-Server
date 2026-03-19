package com.laptrinhjavaweb.news.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    private String userName;

    private String password;
    private String fullName;
    private String email;
    private String imgUrl;
    private int status;
    private String phone;
    private String resetPasswordToken;
    private LocalDate dateOfBirth;

    private Set<RoleEntity> roles = new HashSet<>();
}
