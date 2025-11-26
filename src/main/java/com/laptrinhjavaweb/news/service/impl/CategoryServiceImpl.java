package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.CategoryInput;
import com.laptrinhjavaweb.news.dto.response.mongo.CategoryDetailsResponseForMobile;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.FoodDocument;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;
import com.laptrinhjavaweb.news.repository.mongo.CategoryRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.repository.mongo.SubCategoryRepository;
import com.laptrinhjavaweb.news.service.CategoryService;
import com.laptrinhjavaweb.news.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final RestaurantTagService restaurantTagService;

    @Override
    public RestaurantDocument createCategory(CategoryInput input) {
        RestaurantDocument restaurantDocument = restaurantService.findById(input.getRestaurant());

        // 1️⃣ Tạo category trước để có _id
        CategoryDocument category = CategoryDocument.builder()
                .title(input.getTitle())
                .image(input.getImage())
                .restaurant(restaurantDocument)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        AtomicReference<CategoryDocument> categoryRef = new AtomicReference<>();
        categoryRef.set(categoryRepository.save(category));
        // 2️⃣ Nếu có subcategory thì lưu sau, gán parentCategoryId = category.getId()
        if (input.getSubCategories() != null && !input.getSubCategories().isEmpty()) {
            List<SubCategoryDocument> subCategories = input.getSubCategories().stream()
                    .map(subInput -> {
                        SubCategoryDocument subCategory = SubCategoryDocument.builder()
                                .title(subInput.getTitle())
                                .parentCategoryId(categoryRef.get().getId()) // gán parent sau khi category đã có id
                                .build();
                        return subCategoryRepository.save(subCategory);
                    })
                    .toList();

            // 3️⃣ Cập nhật lại category với danh sách subCategories đã có parentId
            category.setSubCategories(subCategories);
            category = categoryRepository.save(category);
        }
        List<CategoryDocument> categories = restaurantDocument.getCategories();
        categories.add(category);
        restaurantDocument.setCategories(categories);
        RestaurantDocument restaurantSaved = restaurantRepository.save(restaurantDocument);
        restaurantTagService.updateKeywordAndTag(input.getRestaurant());
        return restaurantSaved;

    }

    @Override
    public RestaurantDocument editCategory(CategoryInput input) {
        // Tìm Category cần cập nhật
        CategoryDocument category = categoryRepository.findById(input.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        // Cập nhật dữ liệu cơ bản
        category.setTitle(input.getTitle());
        category.setImage(input.getImage());
        category.setUpdatedAt(new Date());

        // Cập nhật subCategories nếu có
        if (input.getSubCategories() != null) {
            List<SubCategoryDocument> updatedSubCategories = input.getSubCategories().stream()
                    .map(subInput -> {
                        SubCategoryDocument subCategoryDoc;

                        // Nếu subcategory đã tồn tại → cập nhật
                        if (subInput.getId() != null && !subInput.getId().isEmpty()) {
                            subCategoryDoc = subCategoryRepository.findById(subInput.getId())
                                    .orElse(SubCategoryDocument.builder()
                                            .title(subInput.getTitle())
                                            .parentCategoryId(input.getId())
                                            .build());
                            subCategoryDoc.setTitle(subInput.getTitle());
                            subCategoryDoc.setParentCategoryId(input.getId());
                        }
                        // Nếu là mới → tạo mới
                        else {
                            subCategoryDoc = SubCategoryDocument.builder()
                                    .title(subInput.getTitle())
                                    .parentCategoryId(input.getId())
                                    .build();
                        }

                        return subCategoryRepository.save(subCategoryDoc);
                    })
                    .toList();

            category.setSubCategories(updatedSubCategories);
        }

        // Lưu lại category
        categoryRepository.save(category);

        // Trả về lại Restaurant chứa categories

        return restaurantService.findById(input.getRestaurant());
    }

    @Override
    public List<CategoryDetailsResponseForMobile> fetchCategoryDetailsByStoreIdForMobile(String storeId) {
        RestaurantDocument restaurantDocument = restaurantService.findById(storeId);
        List<CategoryDocument> categoryDocuments = categoryRepository.findByRestaurant(restaurantDocument)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        List<CategoryDetailsResponseForMobile> categoryDetailsResponseForMobiles = categoryDocuments.stream().map(categoryDocument -> {
            CategoryDetailsResponseForMobile response = new CategoryDetailsResponseForMobile();
            response.setId(categoryDocument.getId());
            response.setCategory_name(categoryDocument.getTitle());

            // Lấy food (an toàn)
            FoodDocument food = null;
            if (categoryDocument.getFoods() != null && !categoryDocument.getFoods().isEmpty()) {
                food = categoryDocument.getFoods().getFirst();
                response.setFood_id(food.getId());
            } else {
                response.setFood_id(null); // hoặc "" tùy bạn
            }

            if(categoryDocument.getImage() == null){
                if(food == null){
                    response.setUrl("");
                }
                response.setUrl(food.getImage());
            }else response.setUrl(categoryDocument.getImage());
            return response;
        }).toList();
        return categoryDetailsResponseForMobiles;
    }
}
