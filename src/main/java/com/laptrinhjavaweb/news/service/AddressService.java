package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.mongo.UserDocument;

public interface AddressService {
    UserDocument createAddress(String userId, AddressInput addressInput);
    UserDocument selectAddress(String userId,String addressId);
}
