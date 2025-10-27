package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.constant.DeliveryConstant;
import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.request.mongo.UpdateDeliveryBoundsInput;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateDeliveryBoundRestaurantResponse;
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
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        String slug = setSlug(restaurant.getName());
        restaurant.setSlug(slug);
        Long nextId = restaurantRepository.count() + 1;
        restaurant.setOrderId(nextId);
        restaurant.setOrderPrefix(UniqueIdUtil.generateOrderPrefix());

        restaurant =  restaurantRepository.save(restaurant); // chỉ cần lưu restaurant thôi
        // add them nha hang vao vendor
        owner.getRestaurants().add(restaurant);
        ownerRepository.save(owner);


        return restaurant;
    }
    public String setSlug(String name) {
        if (name != null) {
            // Loại bỏ khoảng trắng ở đầu/cuối, chuyển hết sang lowercase
            String slug = name.trim().toLowerCase();

            // Thay tất cả khoảng trắng hoặc ký tự đặc biệt bằng dấu gạch ngang
            slug = slug.replaceAll("[^a-z0-9]+", "-");

            // Loại bỏ dấu gạch ngang ở đầu/cuối nếu có
            slug = slug.replaceAll("^-|-$", "");

            return slug;
        } else {
            return null;
        }
    }

    @Override
    public List<RestaurantDocument> getAllRestaurants(PageRequest pageable) {
        return restaurantRepository.findAll(pageable).getContent();
    }

    @Override
    public Long count() {
        return restaurantRepository.count();
    }

    @Override
    public UpdateDeliveryBoundRestaurantResponse updateDeliveryBoundsAndLocation(UpdateDeliveryBoundsInput input) {
        RestaurantDocument restaurant = restaurantRepository.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        switch (input.getBoundType()){
            case DeliveryConstant.POINT -> {
                restaurant.setDeliveryBounds(null);
            }
            case DeliveryConstant.POLYGON,DeliveryConstant.RADIUS -> {
                // ✅ Convert bounds to GeoJsonPolygon
                    List<org.springframework.data.geo.Point> points = input.getBounds().getFirst()
                            .stream()
                            .map(coord -> new org.springframework.data.geo.Point(coord.getFirst(), coord.get(1)))
                            .collect(Collectors.toList());
                    restaurant.setDeliveryBounds(new GeoJsonPolygon(points));
            }
        }


        // ✅ Convert location
        if (input.getLocation() != null) {
            restaurant.setLocation(new GeoJsonPoint(
                    input.getLocation().getLongitude(),
                    input.getLocation().getLatitude()
            ));
        }

        restaurant.setAddress(input.getAddress());
        restaurant.setBoundType(input.getBoundType());
        restaurant.setPostCode(input.getPostCode());
        restaurant.setCity(input.getCity());
        restaurant.setCircleBounds(input.getCircleBounds());

        RestaurantDocument saved = restaurantRepository.save(restaurant);

        // ✅ Trả về dạng response chuẩn
        UpdateDeliveryBoundRestaurantResponse response = new UpdateDeliveryBoundRestaurantResponse();
        response.setSuccess(true);
        response.setData(saved);
        return response;
    }

    @Override
    public RestaurantDocument findById(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }

    @Override
    public RestaurantDocument getRestaurantDeliveryZoneInfo(String id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }
}
