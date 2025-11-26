package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateOptionInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.OptionDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.repository.mongo.OptionRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final RestaurantRepository restaurantRepository;
    private final OptionRepository optionRepository;
    private final RestaurantTagService restaurantTagService;

    @Override
    public RestaurantDocument createOptions(CreateOptionInput input) {
        // 1️⃣ Lấy nhà hàng theo ID
        RestaurantDocument restaurant = restaurantRepository.findById(input.getRestaurant())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        // 2️⃣ Tạo danh sách OptionDocument mới
        List<OptionDocument> options = input.getOptions().stream()
                .map(opt -> OptionDocument.builder()
                        .title(opt.getTitle())
                        .description(opt.getDescription())
                        .price(opt.getPrice())
                        .build())
                .toList();
        List<OptionDocument> newOptions = optionRepository.saveAll(options);
        // 3️⃣ Nếu chưa có options thì khởi tạo mới
        if (restaurant.getOptions() == null || restaurant.getOptions().isEmpty()) {
            restaurant.setOptions(newOptions);
        } else {
            restaurant.getOptions().addAll(newOptions);
        }

        // 4️⃣ Lưu nhà hàng (cập nhật options)
        RestaurantDocument restaurantSaved = restaurantRepository.save(restaurant);
        restaurantTagService.updateKeywordAndTag(restaurantSaved.getId());
        return restaurantSaved;
    }
}
