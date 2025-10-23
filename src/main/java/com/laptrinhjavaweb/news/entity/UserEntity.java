package com.laptrinhjavaweb.news.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends BaseEntity {

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_bin")
    private String userName;

    private String password;
    private String fullName;
    private String email;
    private String imgUrl;
    private int status;
    private String phone;
    private String resetPasswordToken;
    private LocalDate dateOfBirth;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "role_detail",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();
}
