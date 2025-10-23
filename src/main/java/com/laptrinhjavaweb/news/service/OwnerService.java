package com.laptrinhjavaweb.news.service;


import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;


import java.util.List;

public interface OwnerService {
    OwnerDocument createOwner(OwnerDocument vendor);
    List<OwnerDocument> getVendors();
    VendorResponse getVendor(String id);
}
