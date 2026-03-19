package com.laptrinhjavaweb.news.service.impl;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.mongo.RiderStatsDocument;
import com.laptrinhjavaweb.news.repository.RiderStatsRepository;
import com.laptrinhjavaweb.news.service.RiderStatService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderStatServiceImpl implements RiderStatService {
    private final RiderStatsRepository riderStatsRepository;

    @Override
    public RiderStatsDocument saveNewRiderStat(String riderId) {
        RiderStatsDocument riderStatsDocument = new RiderStatsDocument();
        riderStatsDocument.setRiderId(riderId);
        return riderStatsRepository.save(riderStatsDocument);
    }
}
