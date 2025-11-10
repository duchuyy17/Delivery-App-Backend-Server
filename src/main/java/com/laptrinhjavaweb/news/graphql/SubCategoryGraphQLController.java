package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.SubCategoryInput;
import com.laptrinhjavaweb.news.mongo.SubCategoryDocument;
import com.laptrinhjavaweb.news.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubCategoryGraphQLController {
    private final SubCategoryService subCategoryService;

    @QueryMapping
    public List<SubCategoryDocument> subCategoriesByParentId(@Argument String parentCategoryId) {
        return subCategoryService.findByParentCategoryId(parentCategoryId);
    }

    @MutationMapping
    public String createSubCategories(@Argument List<SubCategoryInput> subCategories) {
        subCategoryService.createSubCategories(subCategories);
        return "Created sub-categories successfully";
    }

    @MutationMapping
    public String deleteSubCategory(@Argument("_id") String id) {
        return subCategoryService.deleteSubCategory(id);
    }
    @QueryMapping
    public List<SubCategoryDocument> subCategories() {
        return subCategoryService.getAllSubCategories();
    }
}
