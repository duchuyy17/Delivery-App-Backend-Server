package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mongo.BannerDocument;
import com.laptrinhjavaweb.news.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BannerGraphQLController {
    private final BannerService bannerService;
    @MutationMapping
    public BannerDocument createBanner(@Argument BannerInput bannerInput) {
        return bannerService.createBanner(bannerInput);
    }
    @QueryMapping
    public List<BannerDocument> banners() {
        return bannerService.getAllBanners();
    }

    @MutationMapping
    public BannerDocument editBanner(@Argument BannerInput bannerInput) {
        return bannerService.editBanner(bannerInput);
    }
}
