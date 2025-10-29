package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;

import java.util.List;

public interface CuisineService {
    CuisineDocument createCuisine(CuisineDocument cuisine);
    List<CuisineDocument> getAllCuisine();
}
