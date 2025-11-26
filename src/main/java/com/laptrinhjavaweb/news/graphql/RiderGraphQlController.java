package com.laptrinhjavaweb.news.graphql;

import java.text.ParseException;
import java.util.List;

import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.mongo.OrderDocument;
import com.laptrinhjavaweb.news.service.JwtService;
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
    private final JwtService jwtService;

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
       RiderDocument riderDocument =  riderService.getRiderById(id);
       riderDocument.setActive(true);

        return riderDocument;
    }

    @MutationMapping
    public RiderDocument deleteRider(@Argument String id) {
        return riderService.deleteRider(id);
    }

    @MutationMapping
    public AuthData riderLogin(
            @Argument String username,
            @Argument String password,
            @Argument String notificationToken,
            @Argument String timeZone
    ) {
        RiderInput req = new RiderInput();
        req.setUsername(username);
        req.setPassword(password);
        req.setNotificationToken(notificationToken);
        req.setTimeZone(timeZone);

        return riderService.login(req);
    }

    @QueryMapping
    public List<OrderDocument> riderOrders() throws ParseException {
        String riderId = jwtService.getCurrentUserId();
        return riderService.riderOrders(riderId);
    }
    @MutationMapping
    public RiderDocument updateRiderLocation(@Argument String latitude,
                                             @Argument String longitude) throws ParseException {
        return riderService.updateRiderLocation(latitude, longitude);
    }
}
