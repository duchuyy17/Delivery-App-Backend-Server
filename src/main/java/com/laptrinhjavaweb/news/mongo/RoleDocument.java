package com.laptrinhjavaweb.news.mongo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.laptrinhjavaweb.news.entity.PermissionEntity;
import com.laptrinhjavaweb.news.entity.UserEntity;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "roles") // tương đương với @Table(name = "role")
public class RoleDocument {

    @Id
    private String id; // MongoDB dùng id kiểu String (ObjectId)

    private String code;
    private String name;

    // Nếu vẫn muốn liên kết với User
    @DBRef(lazy = true)
    private List<UserDocument> users;

    // Nếu vẫn muốn liên kết với Permission
    @DBRef(lazy = true)
    private Set<PermissionDocument> permissions = new HashSet<>();
}
