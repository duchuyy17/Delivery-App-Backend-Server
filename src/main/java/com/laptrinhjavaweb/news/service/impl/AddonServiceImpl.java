package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateAddonInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.AddonDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.repository.mongo.AddonRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.service.AddonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddonServiceImpl implements AddonService {
    private final AddonRepository addonRepository;
    private final RestaurantRepository restaurantRepository;
    @Override
    public RestaurantDocument createAddons(CreateAddonInput createAddonInput) {
        RestaurantDocument restaurantDocument = restaurantRepository.findById(createAddonInput.getRestaurant())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        List<AddonDocument> addonSaved =  createAddonInput.getAddons().stream().map(
                addonInput -> {
                    AddonDocument addonDocument = AddonDocument.builder()
                            .title(addonInput.getTitle())
                            .description(addonInput.getDescription())
                            .quantityMaximum(addonInput.getQuantityMaximum())
                            .quantityMinimum(addonInput.getQuantityMinimum())
                            .options(addonInput.getOptions())
                            .build();
                    return addonRepository.save(addonDocument);
        }).toList();

        if (restaurantDocument.getAddons() == null || restaurantDocument.getAddons().isEmpty()) {
            restaurantDocument.setAddons(addonSaved);
        } else {
            restaurantDocument.getAddons().addAll(addonSaved);
        }
       return restaurantRepository.save(restaurantDocument);
    }
}
