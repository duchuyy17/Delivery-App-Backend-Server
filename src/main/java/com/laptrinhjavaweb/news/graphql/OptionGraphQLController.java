package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.CreateOptionInput;
import com.laptrinhjavaweb.news.mongo.RestaurantDocument;
import com.laptrinhjavaweb.news.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OptionGraphQLController {
    private final OptionService optionService;

    @MutationMapping
    public RestaurantDocument createOptions(@Argument CreateOptionInput optionInput) {
        return optionService.createOptions(optionInput);
    }
}
