package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.CuisineDocument;
import com.laptrinhjavaweb.news.repository.mongo.CuisineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
