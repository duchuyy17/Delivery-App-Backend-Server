package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class CreateAddonInput {
    private String restaurant;
    private List<AddonInput> addons;
}
