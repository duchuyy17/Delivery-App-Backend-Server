package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.request.mongo.StaffInput;
import com.laptrinhjavaweb.news.dto.request.mongo.VendorInput;
import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.service.AuthenticationService;
import com.laptrinhjavaweb.news.service.OwnerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
                .userType(UserTypeConstant.VENDOR)
                .build();

        return OwnerService.createOwner(Owner);
    }

    @MutationMapping
    public OwnerDocument createStaff(@Argument("staffInput") StaffInput input) {
        OwnerDocument Owner = OwnerDocument.builder()
                .name(input.getName())
                .email(input.getEmail())
                .password(input.getPassword()) // nếu dùng BCrypt thì encode trước khi lưu
                .phoneNumber(input.getPhone())
                .isActive(input.getIsActive())
                .permissions(input.getPermissions())
                .userType(UserTypeConstant.STAFF)
                .build();

        return OwnerService.createOwner(Owner);
    }

    @MutationMapping
    public OwnerDocument editStaff(@Argument("staffInput") StaffInput staffInput) {
        return OwnerService.editStaff(staffInput);
    }

    @MutationMapping
    public OwnerDocument editVendor(@Argument("vendorInput") VendorInput input) {
        return OwnerService.updateOwner(input);
    }
    // ok
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
    public List<OwnerDocument> staffs() {
        return OwnerService.getStaffs();
    }

    @QueryMapping
    public VendorResponse restaurantByOwner(@Argument("id") String id) {
        log.info("id:{}", id);
        return OwnerService.getVendor(id);
    }

    @QueryMapping
    public OwnerDocument getVendor(@Argument String id) {
        return OwnerService.getVendorDocument(id);
    }
}
