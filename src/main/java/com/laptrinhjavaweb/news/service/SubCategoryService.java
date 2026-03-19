package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.SubCategoryInput;
import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;

public interface SubCategoryService {
    List<SubCategoryDocument> findByParentCategoryId(String parentCategoryId);

    void createSubCategories(List<SubCategoryInput> subCategories);

    String deleteSubCategory(String id);

    List<SubCategoryDocument> getAllSubCategories();
}
