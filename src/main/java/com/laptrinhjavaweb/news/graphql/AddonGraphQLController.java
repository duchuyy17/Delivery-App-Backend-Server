package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateAddonInput;
import com.laptrinhjavaweb.news.dto.request.mongo.CreateOptionInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.AddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AddonGraphQLController {
    private final AddonService addonService;

    @MutationMapping
    public RestaurantDocument createAddons(@Argument CreateAddonInput addonInput) {
        return addonService.createAddons(addonInput);
    }

}
