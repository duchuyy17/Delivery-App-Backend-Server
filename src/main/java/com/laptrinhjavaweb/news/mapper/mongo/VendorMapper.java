package com.laptrinhjavaweb.news.mapper.mongo;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.laptrinhjavaweb.news.dto.request.mongo.StaffInput;
import com.laptrinhjavaweb.news.dto.request.mongo.VendorInput;
import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorResponse toVendorResponse(OwnerDocument ownerDocument);
    // ✅ Hàm update chỉ ghi đè các field không null từ VendorInput sang OwnerDocument hiện có
    void updateOwner(VendorInput vendorInput, @MappingTarget OwnerDocument ownerDocument);

    void updateStaff(StaffInput staffInput, @MappingTarget OwnerDocument ownerDocument);
}
