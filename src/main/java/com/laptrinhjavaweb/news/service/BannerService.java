package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mongo.BannerDocument;

public interface BannerService {
    List<BannerDocument> getAllBanners();

    BannerDocument createBanner(BannerInput input);

    BannerDocument editBanner(BannerInput bannerInput);
}
