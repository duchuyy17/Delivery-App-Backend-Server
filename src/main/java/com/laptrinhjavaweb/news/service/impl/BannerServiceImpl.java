package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mapper.mongo.BannerMapper;
import com.laptrinhjavaweb.news.mongo.BannerDocument;
import com.laptrinhjavaweb.news.repository.mongo.BannerRepository;
import com.laptrinhjavaweb.news.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;
    @Override
    public List<BannerDocument> getAllBanners() {
        return  bannerRepository.findAll();
    }

    @Override
    public BannerDocument createBanner(BannerInput input) {
        BannerDocument banner = new BannerDocument();
        banner.setTitle(input.getTitle());
        banner.setDescription(input.getDescription());
        banner.setAction(input.getAction());
        banner.setFile(input.getFile());
        banner.setScreen(input.getScreen());
        banner.setParameters(input.getParameters());

        return bannerRepository.save(banner);
    }

    @Override
    public BannerDocument editBanner(BannerInput bannerInput) {
        BannerDocument banner = bannerRepository.findById(bannerInput.get_id())
                .orElseThrow(() -> new RuntimeException("Banner not found"));
        bannerMapper.updateBanner(bannerInput,banner);
        return bannerRepository.save(banner);
    }
}
