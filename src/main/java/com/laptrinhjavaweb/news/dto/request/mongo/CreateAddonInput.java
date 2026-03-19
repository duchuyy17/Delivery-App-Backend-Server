package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class CreateAddonInput {
    private String restaurant;
    private List<AddonInput> addons;
}
