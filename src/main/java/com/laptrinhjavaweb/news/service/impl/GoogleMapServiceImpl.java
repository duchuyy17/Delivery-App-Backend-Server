package com.laptrinhjavaweb.news.service.impl;

import java.time.Duration;
import java.util.Locale;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.laptrinhjavaweb.news.constant.SystemConstant;
import com.laptrinhjavaweb.news.repository.ZoneRepository;
import com.laptrinhjavaweb.news.service.GoogleMapService;
import com.laptrinhjavaweb.news.util.GeoHashUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleMapServiceImpl implements GoogleMapService {
    private final String API_KEY = "AIzaSyCcm7_Wd7uvmC9YnYLu2JHGWPt6z1MaL1E";
    private final RestTemplate restTemplate = new RestTemplate();
    private final RedisTemplate<String, String> redisTemplate;
    private final ZoneRepository zoneRepository;

    @Override
    public Double getTrafficFactor(double lat1, double lng1, double lat2, double lng2) {
        // dung geoHash tao thanh zone (~1.2km × 0.6km) tai vi tri don hang
        String geoHash = GeoHashUtils.encode(lat2, lng2, 6);
        String redisKey = SystemConstant.TRAFFIC_ZONE_KEY + geoHash;
        // 1. Check cache
        double cached = Double.parseDouble(
                redisTemplate.opsForValue().get(redisKey) == null
                        ? "0"
                        : redisTemplate.opsForValue().get(redisKey));
        if (cached != 0) {
            return cached;
        }
        // 2. Call Google Maps API
        double trafficFactor = fetchTrafficFactorFromGoogle(lat1, lng1, lat2, lng2);
        // 3. Cache 10 phút
        redisTemplate.opsForValue().set(redisKey, String.valueOf(trafficFactor), Duration.ofMinutes(10));
        return trafficFactor;
    }

    @Override
    public long calculateTravelTimeSeconds(double originLat, double originLng, double destLat, double destLng) {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json"
                + "?origins=" + originLat + "," + originLng
                + "&destinations=" + destLat + "," + destLng
                + "&mode=driving"
                +"&departure_time=now"
                + "&key=" + API_KEY;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

        return response.getBody()
                .path("rows")
                .get(0)
                .path("elements")
                .get(0)
                .path("duration")
                .path("value")
                .asLong();
    }

    private double fetchTrafficFactorFromGoogle(double lat1, double lng1, double lat2, double lng2) {
        String url = String.format(
                Locale.US,
                "https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%f,%f&departure_time=now&key=%s",
                lat1,
                lng1,
                lat2,
                lng2,
                API_KEY);

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);
            JsonNode element = response.path("rows").get(0).path("elements").get(0);

            // 1. Thời gian đi bình thường (không tắc đường) - tính bằng giây
            long duration = element.path("duration").path("value").asLong();

            // 2. Thời gian đi thực tế (có tính tới traffic) - tính bằng giây
            long durationInTraffic =
                    element.path("duration_in_traffic").path("value").asLong();

            if (duration == 0) return 1.0;
            System.out.println("duration: " + duration);
            System.out.println("durationInTraffic: " + durationInTraffic);
            // Tính toán hệ số tắc đường
            // Ví dụ: Bình thường đi 10p, giờ đi 15p -> factor = 1.5
            return (double) durationInTraffic / duration;

        } catch (Exception e) {
            return 1.0; // Fail-safe: Nếu lỗi API thì coi như không tắc đường
        }
    }
}
