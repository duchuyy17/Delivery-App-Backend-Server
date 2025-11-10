package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.SubCategoryInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.CategoryDocument;
import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;
import com.laptrinhjavaweb.news.repository.mongo.CategoryRepository;
import com.laptrinhjavaweb.news.repository.mongo.SubCategoryRepository;
import com.laptrinhjavaweb.news.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public List<SubCategoryDocument> findByParentCategoryId(String parentCategoryId) {
        return subCategoryRepository.findByParentCategoryId(parentCategoryId);
    }

    @Override
    public void createSubCategories(List<SubCategoryInput> subCategories) {
        // 1️⃣ Lấy CategoryDocument cha
        CategoryDocument categoryDocument = categoryRepository.findById(subCategories.getFirst().getParentCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // 2️⃣ Tạo danh sách SubCategoryDocument mới
        List<SubCategoryDocument> subCategoryDocs = subCategories.stream()
                .map(input -> SubCategoryDocument.builder()
                        .title(input.getTitle())
                        .parentCategoryId(input.getParentCategoryId())
                        .build())
                .toList();

        // 3️⃣ Lưu trước vào DB để Mongo sinh _id
        List<SubCategoryDocument> savedSubCategories = subCategoryRepository.saveAll(subCategoryDocs);

        // 4️⃣ Gộp danh sách con mới vào CategoryDocument
        List<SubCategoryDocument> existingSubCategories = categoryDocument.getSubCategories();
        if (existingSubCategories == null) {
            existingSubCategories = new ArrayList<>();
        }
        existingSubCategories.addAll(savedSubCategories);

        categoryDocument.setSubCategories(existingSubCategories);

        // 5️⃣ Lưu lại CategoryDocument
        categoryRepository.save(categoryDocument);
    }

    @Override
    public String deleteSubCategory(String id) {
        SubCategoryDocument subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SUBCATEGORY_NOT_FOUND));
        // Xóa subcategory ra khỏi Category cha (nếu có)
        if (subCategory.getParentCategoryId() != null) {
            categoryRepository.findById(subCategory.getParentCategoryId()).ifPresent(category -> {
                List<SubCategoryDocument> subCategories = category.getSubCategories();
                if (subCategories != null) {
                    subCategories.removeIf(sc -> sc.getId().equals(id));
                    category.setSubCategories(subCategories);
                    categoryRepository.save(category);
                }
            });
        }
        subCategoryRepository.deleteById(id);
        return "Deleted sub-category successfully";
    }

    @Override
    public List<SubCategoryDocument> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

}
