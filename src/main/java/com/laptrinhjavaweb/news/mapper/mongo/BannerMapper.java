package com.laptrinhjavaweb.news.mapper.mongo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.mongo.BannerInput;
import com.laptrinhjavaweb.news.mongo.BannerDocument;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    void updateBanner(BannerInput bannerInput, @MappingTarget BannerDocument banner);
}
