package com.laptrinhjavaweb.news.entity;
import java.util.ArrayList; import java.util.List;
import java.util.Set; import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity @Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RoleEntity extends BaseEntity {
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users = new ArrayList<>();
    @ManyToMany
    private Set<PermissionEntity> permissions;
}