package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;
import com.laptrinhjavaweb.news.service.ConfigurationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ConfigurationGraphQLController {
    private final ConfigurationService configurationService;

    @QueryMapping
    public ConfigurationDocument configuration() {
        return configurationService.getConfiguration();
    }

    @MutationMapping
    public ConfigurationDocument createConfiguration(@Argument("input") ConfigurationDocument input) {
        return configurationService.save(input);
    }
}
