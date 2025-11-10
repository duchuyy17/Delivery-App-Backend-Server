package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.ZoneInput;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.service.ZoneService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ZoneGraphQLController {
    private final ZoneService zoneService;

    @QueryMapping
    public List<ZoneDocument> zones() {
        return zoneService.findAll();
    }

    @QueryMapping
    public ZoneDocument zoneById(@Argument String id) {
        return zoneService.findById(id);
    }

    @MutationMapping
    public ZoneDocument createZone(@Argument("zone") ZoneInput input) {
        return zoneService.createZone(input);
    }

    @MutationMapping
    public ZoneDocument updateZone(@Argument String id, @Argument("input") ZoneDocument input) {
        return zoneService.update(id, input);
    }

    @MutationMapping
    public Boolean deleteZone(@Argument String id) {
        return zoneService.delete(id);
    }
}
