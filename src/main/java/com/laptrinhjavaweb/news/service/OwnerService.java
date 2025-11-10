package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.StaffInput;
import com.laptrinhjavaweb.news.dto.request.mongo.VendorInput;
import com.laptrinhjavaweb.news.dto.response.mongo.VendorResponse;
import com.laptrinhjavaweb.news.mongo.OwnerDocument;

public interface OwnerService {
    OwnerDocument createOwner(OwnerDocument vendor);

    List<OwnerDocument> getVendors();

    List<OwnerDocument> getStaffs();

    VendorResponse getVendor(String id);

    OwnerDocument getVendorDocument(String id);

    OwnerDocument updateOwner(VendorInput vendor);

    OwnerDocument editStaff(StaffInput input);
}
