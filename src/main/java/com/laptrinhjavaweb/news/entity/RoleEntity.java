package com.laptrinhjavaweb.news.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleEntity extends BaseEntity {

    private String code;

    private String name;

    private List<UserEntity> users = new ArrayList<>();

    private Set<PermissionEntity> permissions;
}
