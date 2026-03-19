package com.laptrinhjavaweb.news.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PermissionEntity extends BaseEntity {

    private String code;

    private String name;

    private List<RoleEntity> roles = new ArrayList<>();
}
