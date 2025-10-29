package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.repository.mongo.RiderRepository;
import com.laptrinhjavaweb.news.repository.mongo.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    @Override
    public List<RiderDocument> getAllRiders() {
        return riderRepository.findAll();
    }

    @Override
    public RiderDocument createRider(RiderInput input) {
        ZoneDocument zone = null;

        if (input.getZone() != null) {
            zone = zoneRepository.findById(input.getZone())
                    .orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_FOUND));
        }

        RiderDocument rider = RiderDocument.builder()
                .name(input.getName())
                .username(input.getUsername())
                .password(input.getPassword())
                .phone(input.getPhone())
                .available(input.getAvailable() != null ? input.getAvailable() : false)
                .vehicleType(input.getVehicleType())
                .zone(zone)
                .build();

        return riderRepository.save(rider);
    }

    @Override
    public RiderDocument getRiderById(String id) {
        return riderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
    }

    @Override
    public RiderDocument deleteRider(String id) {
        RiderDocument rider = riderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        riderRepository.delete(rider);
        return rider;
    }
}
