package com.laptrinhjavaweb.news.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.laptrinhjavaweb.news.dto.AbstractDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse extends AbstractDTO {

    String code;
    String name;
    Set<PermissionResponse> permissions;
}
