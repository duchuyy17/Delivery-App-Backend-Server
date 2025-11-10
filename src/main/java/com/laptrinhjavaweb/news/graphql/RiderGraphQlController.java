package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.service.RiderService;

import lombok.RequiredArgsConstructor;

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

    @MutationMapping
    public RiderDocument editRider(@Argument("riderInput") RiderInput riderInput) {
        return riderService.editRider(riderInput);
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
