package com.laptrinhjavaweb.news.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData extends BaseEntity {

    private String name;

    private byte[] imageData;

    private String type;
}
