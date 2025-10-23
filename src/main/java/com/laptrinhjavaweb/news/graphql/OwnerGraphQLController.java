package com.laptrinhjavaweb.news.graphql;


import com.laptrinhjavaweb.news.dto.request.mongo.VendorInput;
import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.service.AuthenticationService;
import com.laptrinhjavaweb.news.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OwnerGraphQLController {
    private final OwnerService OwnerService;
    private final AuthenticationService<OwnerDocument> authenticationService;

    @MutationMapping
    public OwnerDocument createVendor(@Argument("vendorInput") VendorInput input) {
        OwnerDocument Owner = OwnerDocument.builder()
                .name(input.getName())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .password(input.getPassword()) // nếu dùng BCrypt thì encode trước khi lưu
                .image(input.getImage())
                .phoneNumber(input.getPhoneNumber())
                .isActive(true)
                .userType("VENDOR")
                .build();

        return OwnerService.createOwner(Owner);
    }

    @MutationMapping
    public OwnerDocument ownerLogin(@Argument String email, @Argument String password) {
        return authenticationService.login(email, password);
    }

    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    @QueryMapping
    public List<OwnerDocument> vendors() {
        return OwnerService.getVendors();
    }

    @QueryMapping
    public VendorResponse restaurantByOwner(@Argument("id") String id) {
            log.info("id:{}",id);
        return OwnerService.getVendor(id);
    }

}
