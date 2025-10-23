package com.laptrinhjavaweb.news.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PermissionEntity extends BaseEntity {
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<RoleEntity> roles = new ArrayList<>();
}
