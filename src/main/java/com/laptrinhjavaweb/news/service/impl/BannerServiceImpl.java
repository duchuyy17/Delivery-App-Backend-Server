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
        BannerDocument b1 = new BannerDocument();
        b1.setId("69031733f8e5a0d4daa440d2");
        b1.setTitle("Welcome to Enatega");
        b1.setDescription("Delivery in Minutess");
        b1.setAction("Navigate Specific Page");
        b1.setScreen("Grocery List");
        b1.setFile("https://enatega-backend.s3.eu-north-1.amazonaws.com/f208a326-43c8-4f44-8f7d-348c75534390.jpeg");

        BannerDocument b2 = new BannerDocument();
        b2.setId("6889f70875e0b8822a04f197");
        b2.setTitle("🚴 שליח בדרך אליך!");
        b2.setDescription("תזמין – ותוך כמה דקות אנחנו אצלך.");
        b2.setAction("Navigate Specific Page");
        b2.setScreen("Grocery List");
        b2.setFile("https://enatega-backend.s3.eu-north-1.amazonaws.com/gnoasivedvs5dn8u3w5j.mp4");

        BannerDocument b3 = new BannerDocument();
        b3.setId("6673466b0caf7685fcfe0890");
        b3.setTitle("אתה מוכן לזה?");
        b3.setDescription("הצעות בלעדיות כל היום");
        b3.setAction("Navigate Specific Page");
        b3.setScreen("Grocery List");
        b3.setFile("https://enatega-backend.s3.eu-north-1.amazonaws.com/yzreto2xvts8jihwmn4h.mp4");
        List<BannerDocument> bannerDocumentList = new ArrayList<>(Arrays.asList(b1, b2, b3));
        bannerDocumentList.addAll(bannerRepository.findAll());
        return bannerDocumentList;
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
