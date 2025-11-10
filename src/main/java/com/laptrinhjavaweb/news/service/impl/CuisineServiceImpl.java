package com.laptrinhjavaweb.news.service.impl;

import java.util.List;

import com.laptrinhjavaweb.news.service.CuisineService;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;
import com.laptrinhjavaweb.news.repository.mongo.CuisineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuisineServiceImpl implements CuisineService {
    private final CuisineRepository cuisineRepository;

    @Override
    public CuisineDocument createCuisine(CuisineDocument cuisine) {
        return cuisineRepository.save(cuisine);
    }

    @Override
    public List<CuisineDocument> getAllCuisine() {
        return cuisineRepository.findAll();
    }
}
