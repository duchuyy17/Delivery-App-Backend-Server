package com.laptrinhjavaweb.news.mongo;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users") // tương đương @Table(name = "user")
public class UserDocument {
    @Id
    private String id; //

    private String userName;
    private String password;
    private String fullName;
    private String email;
    private String imgUrl;
    private int status;
    private String phone;
    private String resetPasswordToken;
    private LocalDate dateOfBirth;

    // Tham chiếu tới RoleDocument (thay cho ManyToMany)
    @DBRef
    private Set<RoleDocument> roles = new HashSet<>();
}
