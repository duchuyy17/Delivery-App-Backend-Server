package com.laptrinhjavaweb.news.dto.request;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateRequest {
    String code;
    String name;
    Set<Long> permissions;
}
