package com.laptrinhjavaweb.news.dto;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractDTO {
    String id;
    String createBy;
    String modifiedBy;
    Date createDate;
    Date modifiedDate;
}
