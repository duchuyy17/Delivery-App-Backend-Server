package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.ZoneInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.ZoneDocument;
import com.laptrinhjavaweb.news.repository.mongo.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.stereotype.Service;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    @Override
    public ZoneDocument findById(String id) {
        return zoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_EXISTED));
    }

    @Override
    public ZoneDocument createZone(ZoneInput input) {
        // Lấy list điểm đầu tiên trong polygon
        List<Point> points = input.getCoordinates().get(0)
                .stream()
                .map(coord -> new Point(coord.get(0), coord.get(1)))
                .collect(Collectors.toList());

        GeoJsonPolygon polygon = new GeoJsonPolygon(points);

        ZoneDocument zone = new ZoneDocument();
        zone.setTitle(input.getTitle());
        zone.setDescription(input.getDescription());
        zone.setLocation(polygon);
        zone.setIsActive(true);

        return zoneRepository.save(zone);
    }

    @Override
    public List<ZoneDocument> findAll() {
        return zoneRepository.findAll();
    }

    @Override
    public ZoneDocument update(String id, ZoneDocument input) {
        ZoneDocument existing = zoneRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ZONE_NOT_EXISTED));
        if (existing == null) return null;

        existing.setTitle(input.getTitle());
        existing.setDescription(input.getDescription());
        existing.setLocation(input.getLocation2());
        existing.setIsActive(input.getIsActive());
        return zoneRepository.save(existing);
    }

    @Override
    public Boolean delete(String id) {
        if (!zoneRepository.existsById(id)) return false;
        zoneRepository.deleteById(id);
        return true;
    }
}
