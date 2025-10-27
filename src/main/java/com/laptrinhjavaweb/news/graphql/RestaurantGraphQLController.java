package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.data.CircleBounds;
import com.laptrinhjavaweb.news.dto.request.mongo.CircleBoundsInput;
import com.laptrinhjavaweb.news.dto.request.mongo.CoordinatesInput;
import com.laptrinhjavaweb.news.dto.request.mongo.RestaurantInput;
import com.laptrinhjavaweb.news.dto.request.mongo.UpdateDeliveryBoundsInput;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPaginationResponse;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateDeliveryBoundRestaurantResponse;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RestaurantGraphQLController {
    private final RestaurantService restaurantService;

    @MutationMapping
    public RestaurantDocument createRestaurant(
            @Argument("restaurant") RestaurantInput restaurantInput,
            @Argument("owner") String ownerId
    ) {
        return restaurantService.createRestaurant(restaurantInput, ownerId);
    }

    @QueryMapping
    public RestaurantPaginationResponse restaurantsPaginated(@Argument("page") Integer page,
                                                             @Argument("limit") Integer limit) {
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
    public UpdateDeliveryBoundRestaurantResponse updateDeliveryBoundsAndLocation(
            @Argument("id") String id,
            @Argument("boundType") String boundType,
            @Argument("bounds") List<List<List<Double>>> bounds,
            @Argument("circleBounds") CircleBounds circleBounds,
            @Argument("location") CoordinatesInput location,
            @Argument("address") String address,
            @Argument("postCode") String postCode,
            @Argument("city") String city
    ) {
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
}
