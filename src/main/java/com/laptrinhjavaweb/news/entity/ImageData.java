package com.laptrinhjavaweb.news.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ImageData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageData extends BaseEntity {

    private String name;

    @Lob
    @Column(name = "imagedata", length = 1000)
    private byte[] imageData;

    private String type;
}
