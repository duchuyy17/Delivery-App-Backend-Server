package com.laptrinhjavaweb.news.mongo;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.laptrinhjavaweb.news.dto.data.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users") // tương đương @Table(name = "user")
public class UserDocument {
    @Id
    private String id; //
    private String name;
    private String email;
    private String phone;
    private Date createdAt;
    private String userName;
    private String password;
    private String fullName;
    private String userType;
    private String status;
    private Date lastLogin;
    private String note;
    private String imgUrl;
    private HashSet<Address> addresses = new HashSet<>();

    private String resetPasswordToken;
    private LocalDate dateOfBirth;

    // Tham chiếu tới RoleDocument (thay cho ManyToMany)
    @DBRef
    private Set<RoleDocument> roles = new HashSet<>();

    public long getCreatedAt() {
        return createdAt != null ? createdAt.getTime() : 0;
    }
    private String get_id(){
        return id;
    }
}
