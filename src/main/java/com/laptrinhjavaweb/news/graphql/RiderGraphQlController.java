package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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

    @MutationMapping
    public RiderDocument createRider(@Argument RiderInput riderInput) {
        return riderService.createRider(riderInput);
    }
    @QueryMapping
    public RiderDocument rider(@Argument String id) {
        return riderService.getRiderById(id);
    }
    @MutationMapping
    public RiderDocument deleteRider(@Argument String id) {
        return riderService.deleteRider(id);
    }
}
