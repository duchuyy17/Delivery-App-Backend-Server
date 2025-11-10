package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mongo.BannerDocument;

import java.util.List;

public interface BannerService {
    List<BannerDocument> getAllBanners();
    BannerDocument createBanner(BannerInput input);
    BannerDocument editBanner(BannerInput bannerInput);
}
