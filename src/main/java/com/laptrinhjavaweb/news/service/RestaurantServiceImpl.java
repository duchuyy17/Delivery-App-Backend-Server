package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.RestaurantMapper;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.repository.mongo.OwnerRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.util.UniqueIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    @Override
    public RestaurantDocument createRestaurant(RestaurantInput restaurantInput, String ownerId) {
        OwnerDocument owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_EXISTED));
        RestaurantDocument restaurant = restaurantMapper.toRestaurantDocument(restaurantInput);
        restaurant.setOwner(owner);
        if (restaurantInput.getLocation() != null && restaurantInput.getLocation().getCoordinates() != null) {
            var coords = restaurantInput.getLocation().getCoordinates();
            restaurant.setLocation(new GeoJsonPoint(coords.get(0), coords.get(1)));
        }
        restaurant.setUniqueRestaurantId(UniqueIdUtil.generateRestaurantUniqueId(owner.getUniqueId()));
        // add them nha hang vao vendor
        restaurant =  restaurantRepository.save(restaurant); // chỉ cần lưu restaurant thôi

        owner.getRestaurants().add(restaurant);
        ownerRepository.save(owner);


        return restaurant;
    }

    @Override
    public List<RestaurantDocument> getAllRestaurants(PageRequest pageable) {
        return restaurantRepository.findAll(pageable).getContent();
    }

    @Override
    public Long count() {
        return restaurantRepository.count();
    }
}
