package com.laptrinhjavaweb.news.mapper.mongo;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mongo.BannerDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface BannerMapper {
    void updateBanner(BannerInput bannerInput, @MappingTarget BannerDocument banner);
}
