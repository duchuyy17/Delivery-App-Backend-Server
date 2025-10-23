package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.repository.mongo.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    @Override
    public List<RiderDocument> getAllRiders() {
        return riderRepository.findAll();
    }
}
