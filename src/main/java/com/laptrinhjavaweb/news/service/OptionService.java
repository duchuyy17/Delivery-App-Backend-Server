package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateOptionInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

public interface OptionService {
    RestaurantDocument createOptions(CreateOptionInput input);
}
