package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.RiderStatsDocument;

public interface RiderStatService {
    RiderStatsDocument saveNewRiderStat(String riderId);
}
