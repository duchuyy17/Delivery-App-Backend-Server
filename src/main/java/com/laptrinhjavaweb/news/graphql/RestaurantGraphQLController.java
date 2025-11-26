package com.laptrinhjavaweb.news.graphql;

import java.util.Collections;
import java.util.List;


import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.response.mongo.NearByRestaurantsPreview;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPreview;
import com.laptrinhjavaweb.news.mongo.SectionInfoDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.CircleBounds;
import com.laptrinhjavaweb.news.dto.request.mongo.*;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPaginationResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateRestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RestaurantGraphQLController {
    private final RestaurantService restaurantService;

    @MutationMapping
    public RestaurantDocument createRestaurant(
            @Argument("restaurant") RestaurantInput restaurantInput, @Argument("owner") String ownerId) {
        return restaurantService.createRestaurant(restaurantInput, ownerId);
    }

    @MutationMapping
    public RestaurantDocument editRestaurant(@Argument("restaurant") RestaurantProfileInput restaurant) {
        return restaurantService.editRestaurant(restaurant);
    }

    @MutationMapping
    public UpdateRestaurantResponse updateRestaurantBussinessDetails(
            @Argument String id, @Argument BussinessDetailsInput bussinessDetails) {
        return restaurantService.updateRestaurantBussinessDetails(id, bussinessDetails);
    }

    @QueryMapping
    public RestaurantPaginationResponse restaurantsPaginated(
            @Argument("page") Integer page, @Argument("limit") Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        List<RestaurantDocument> restaurantDocuments = restaurantService.getAllRestaurants(pageable);
        Long totalItem = restaurantService.count();
        Long totalPage = (long) Math.ceil((double) totalItem / limit);

        return RestaurantPaginationResponse.builder()
                .data(restaurantDocuments)
                .currentPage(page)
                .totalPages(totalPage)
                .totalCount(totalItem)
                .build();
    }

    @MutationMapping
    public UpdateRestaurantResponse updateDeliveryBoundsAndLocation(
            @Argument("id") String id,
            @Argument("boundType") String boundType,
            @Argument("bounds") List<List<List<Double>>> bounds,
            @Argument("circleBounds") CircleBounds circleBounds,
            @Argument("location") CoordinatesInput location,
            @Argument("address") String address,
            @Argument("postCode") String postCode,
            @Argument("city") String city) {
        UpdateDeliveryBoundsInput input = new UpdateDeliveryBoundsInput();
        input.setId(id);
        input.setBoundType(boundType);
        input.setBounds(bounds);
        input.setCircleBounds(circleBounds);
        input.setLocation(location);
        input.setAddress(address);
        input.setPostCode(postCode);
        input.setCity(city);

        return restaurantService.updateDeliveryBoundsAndLocation(input);
    }

    @QueryMapping
    public RestaurantDocument restaurant(@Argument String id) {
        return restaurantService.findById(id);
    }

    @QueryMapping
    public RestaurantDocument getRestaurantDeliveryZoneInfo(@Argument String id) {
        return restaurantService.getRestaurantDeliveryZoneInfo(id);
    }

    @MutationMapping
    public RestaurantDocument updateTimings(@Argument String id, @Argument List<TimingsInput> openingTimes) {
        return restaurantService.updateTimings(id, openingTimes);
    }
    @QueryMapping
    public List<RestaurantDocument> restaurants() {
        return restaurantService.getAllRestaurant();
    }
    @QueryMapping
    public List<RestaurantDocument> nearbyRestaurants(
            @Argument double longitude,
            @Argument double latitude,
            @Argument double radiusKm) {
        return restaurantService.findNearbyRestaurants(longitude, latitude, radiusKm);
    }
    @QueryMapping
    public NearByRestaurantsPreview nearByRestaurantsPreview(
            @Argument Double latitude,
            @Argument Double longitude,
            @Argument String shopType
    ) {
        List<RestaurantPreview> restaurantPreviews =   restaurantService.findNearByLocation(longitude, latitude, shopType);
//        SectionInfoDocument section = SectionInfoDocument.builder()
//                .id("66b44629329c70266a0269d2")
//                .name("add section")
//                .restaurants(List.of("691162a52978894ec43f20a3"))
//                .build();
        return NearByRestaurantsPreview.builder()
                .offers(Collections.emptyList())
                .sections(Collections.emptyList())
                .restaurants(restaurantPreviews)
                .build();
    }
    @QueryMapping
    public List<RestaurantPreview> mostOrderedRestaurantsPreview(
            @Argument Double latitude,
            @Argument Double longitude
    ) {
        return restaurantService.getMostOrderedRestaurants(latitude, longitude);
    }

    @QueryMapping
    public List<RestaurantPreview> recentOrderRestaurantsPreview(
            @Argument Double latitude,
            @Argument Double longitude
    ) {
        return restaurantService.getMostOrderedRestaurants(latitude, longitude);
    }

    @MutationMapping
    public AuthData restaurantLogin(@Argument String username,
                                    @Argument String password,
                                    @Argument String notificationToken) {
        return restaurantService.login(username,password);
    }

}
