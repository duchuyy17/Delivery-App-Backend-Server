package com.laptrinhjavaweb.news.dto.response.mongo;

import com.laptrinhjavaweb.news.mongo.OfferDocument;
import com.laptrinhjavaweb.news.mongo.SectionInfoDocument;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NearByRestaurantsPreview {
    private List<OfferDocument> offers;
    private List<SectionInfoDocument> sections;
    private List<RestaurantPreview> restaurants;
}
