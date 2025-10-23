package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RiderGraphQlController {
    private final RiderService riderService;

    @QueryMapping
    public List<RiderDocument> riders() {
        return riderService.getAllRiders();
    }
}
