package com.laptrinhjavaweb.news.service.impl;

import java.util.List;

import com.laptrinhjavaweb.news.service.RiderService;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.request.mongo.RiderInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.RiderMapper;
import com.laptrinhjavaweb.news.mongo.RiderDocument;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.repository.mongo.RiderRepository;
import com.laptrinhjavaweb.news.repository.mongo.ZoneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {
    private final RiderRepository riderRepository;
    private final ZoneRepository zoneRepository;
    private final RiderMapper riderMapper;

    @Override
    public List<RiderDocument> getAllRiders() {
        return riderRepository.findAll();
    }

    @Override
    public RiderDocument createRider(RiderInput input) {
        ZoneDocument zone = null;

        if (input.getZone() != null) {
            zone = zoneRepository
                    .findById(input.getZone())
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
        return riderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
    }

    @Override
    public RiderDocument deleteRider(String id) {
        RiderDocument rider =
                riderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        riderRepository.delete(rider);
        return rider;
    }

    @Override
    public RiderDocument editRider(RiderInput input) {
        RiderDocument existing =
                riderRepository.findById(input.getId()).orElseThrow(() -> new AppException(ErrorCode.RIDER_NOT_FOUND));
        if (input.getZone() != null) {
            ZoneDocument zone = zoneRepository
                    .findById(input.getZone())
                    .orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_FOUND));
            existing.setZone(zone);
        }

        riderMapper.updateRider(input, existing);

        return riderRepository.save(existing);
    }
}
