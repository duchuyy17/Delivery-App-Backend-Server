package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateAddonInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface AddonService {
    RestaurantDocument createAddons(CreateAddonInput createAddonInput);
}
