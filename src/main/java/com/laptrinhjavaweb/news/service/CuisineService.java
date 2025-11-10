package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;

public interface CuisineService {
    CuisineDocument createCuisine(CuisineDocument cuisine);

    List<CuisineDocument> getAllCuisine();
}
