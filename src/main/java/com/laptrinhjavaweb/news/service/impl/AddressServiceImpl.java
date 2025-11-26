package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.AddressInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.AddressDocument;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.AddressRepository;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;
import com.laptrinhjavaweb.news.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserV1Repository userRepository;
    @Override
    public UserDocument createAddress(String userId, AddressInput addressInput) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        AddressDocument address = AddressDocument.builder()
                .label(addressInput.getLabel())
                .deliveryAddress(addressInput.getDeliveryAddress())
                .details(addressInput.getDetails())
                .location(new GeoJsonPoint(Double.parseDouble(addressInput.getLongitude()),
                        Double.parseDouble(addressInput.getLatitude())))
                .selected(true)
                .build();
        // nếu địa chỉ mới được chọn, bỏ chọn các địa chỉ khác
        if (address.isSelected()) {
            user.getAddresses().forEach(a -> a.setSelected(false));
        }
        AddressDocument addressSaved = addressRepository.save(address);
        user.getAddresses().add(addressSaved);
        return userRepository.save(user);
    }

    @Override
    public UserDocument selectAddress(String userId, String addressId) {
        UserDocument user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.getAddresses().forEach(a -> a.setSelected(a.getId().equals(addressId)));
        return userRepository.save(user);
    }
}
