package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;

import com.laptrinhjavaweb.news.repository.mongo.FoodRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantTagService {
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    public void updateKeywordAndTag(String restaurantId) {

        RestaurantDocument restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        Set<String> tags = new HashSet<>(generateBasicTags(restaurant));
        Set<String> keywords = new HashSet<>(generateBasicKeywords(restaurant));
        List<CategoryDocument> categories = restaurant.getCategories();
        for (CategoryDocument c : categories) {
            tags.add(c.getTitle());
            keywords.add(c.getTitle());
        }
        List<FoodDocument> foods = foodRepository.findByRestaurant(restaurantId)
                .orElseThrow(() -> new AppException(ErrorCode.FOOD_NOT_FOUND));
        for (FoodDocument f : foods) {
            if (f.getTitle() != null) {
                keywords.add(f.getTitle());
            }
//             5️⃣ Variations → keywords
            if (f.getVariations() != null) {
                f.getVariations().forEach(variation -> {
                    if (variation.getTitle() != null) {
                        keywords.add(variation.getTitle());
                    }
                });
            }
        }
//         6️⃣ Addons → keywords
        if (restaurant.getAddons() != null) {
            restaurant.getAddons().forEach(addon -> {
                if (addon.getTitle() != null) {
                    keywords.add(addon.getTitle());
                }

            });
        }
        // 7️⃣ Options → keywords
        if (restaurant.getOptions() != null) {
            restaurant.getOptions().forEach(opt -> keywords.add(opt.getTitle()));
        }
        restaurant.setTags(new ArrayList<>(tags));
        restaurant.setKeywords(new ArrayList<>(keywords));

        restaurantRepository.save(restaurant);
    }

    public List<String> generateBasicTags(RestaurantDocument r) {
        return new ArrayList<>();
    }

    public List<String> generateBasicKeywords(RestaurantDocument r) {
        List<String> list = new ArrayList<>();
        if (r.getName() != null) list.add(r.getName().toLowerCase());
        if (r.getSlug() != null) list.add(r.getSlug());
        return list;
    }

}
