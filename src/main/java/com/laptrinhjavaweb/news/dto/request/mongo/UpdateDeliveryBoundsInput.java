package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import com.laptrinhjavaweb.news.dto.data.CircleBounds;

import lombok.Data;

@Data
public class UpdateDeliveryBoundsInput {
    private String id;
    private String boundType;
    private List<List<List<Double>>> bounds;
    private CircleBounds circleBounds;
    private CoordinatesInput location;
    private String address;
    private String postCode;
    private String city;
}
