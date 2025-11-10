package com.laptrinhjavaweb.news.service;


import com.laptrinhjavaweb.news.dto.request.mongo.SubCategoryInput;
import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryDocument> findByParentCategoryId(String parentCategoryId);
    void createSubCategories(List<SubCategoryInput> subCategories);
    String deleteSubCategory(String id);
    List<SubCategoryDocument> getAllSubCategories();

}
