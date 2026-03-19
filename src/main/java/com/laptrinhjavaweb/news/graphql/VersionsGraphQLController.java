package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.VersionsDTO;
import com.laptrinhjavaweb.news.service.VersionsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VersionsGraphQLController {
    private final VersionsService versionsService;

    @QueryMapping
    public VersionsDTO getVersions() {
        return versionsService.getVersions();
    }
}
