package com.laptrinhjavaweb.news.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "permissions") // tương đương @Table(name = "permission")
public class PermissionDocument {

    @Id
    private String id;

    private String code;
    private String name;

    // Liên kết với RoleEntityV1 (nếu muốn giữ quan hệ 2 chiều)
    @DBRef(lazy = true)
    private List<RoleDocument> roles = new ArrayList<>();
}
