package com.laptrinhjavaweb.news.mapper.mongo;


import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorResponse toVendorResponse(OwnerDocument ownerDocument);
}
