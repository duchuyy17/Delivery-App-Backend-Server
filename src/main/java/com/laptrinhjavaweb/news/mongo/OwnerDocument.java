package com.laptrinhjavaweb.news.mongo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "owner")
public class OwnerDocument {

    @Id
    @JsonProperty("_id")
    private String id;

    @Field("unique_id")
    private String uniqueId;
    private String email;
    private String firstName;
    private String lastName;
    private String userType;
    private String userTypeId;
    @Builder.Default
    private boolean isActive = true;
    private String name;
    private String image;
    private String phoneNumber;
    private String password;
    private String plainPassword;
    private String token;
    @DBRef(lazy = true)
    private List<RestaurantDocument> restaurants = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();

    // Nếu schema dùng "_id" thì thêm resolver
    public String get_id() {
        return id;
    }
    public String getunique_id(){
        return uniqueId;
    }
}
