package com.laptrinhjavaweb.news.service.impl;

import java.text.Normalizer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.laptrinhjavaweb.news.constant.RestaurantTypeConstant;
import com.laptrinhjavaweb.news.dto.data.AuthData;
import com.laptrinhjavaweb.news.dto.response.mongo.NearByRestaurantsPreview;
import com.laptrinhjavaweb.news.dto.response.mongo.RestaurantPreview;
import com.laptrinhjavaweb.news.mongo.*;
import com.laptrinhjavaweb.news.repository.mongo.FoodRepository;
import com.laptrinhjavaweb.news.service.JwtService;
import com.laptrinhjavaweb.news.service.RestaurantService;
import com.laptrinhjavaweb.news.service.ZoneService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laptrinhjavaweb.news.constant.DeliveryConstant;
import com.laptrinhjavaweb.news.constant.UserTypeConstant;
import com.laptrinhjavaweb.news.dto.data.BussinessDetails;
import com.laptrinhjavaweb.news.dto.data.OpeningTimes;
import com.laptrinhjavaweb.news.dto.data.Timings;
import com.laptrinhjavaweb.news.dto.request.mongo.*;
import com.laptrinhjavaweb.news.dto.response.mongo.UpdateRestaurantResponse;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mapper.mongo.RestaurantMapper;
import com.laptrinhjavaweb.news.repository.mongo.OwnerRepository;
import com.laptrinhjavaweb.news.repository.mongo.RestaurantRepository;
import com.laptrinhjavaweb.news.util.UniqueIdUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final OwnerRepository ownerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private final ZoneService zoneService;
    private final PasswordEncoder passwordEncoder;
    private final FoodRepository foodRepository;
    private final RestaurantTagService restaurantTagService;
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    private Long REFRESH_DURATION;

    @Transactional
    @Override
    public RestaurantDocument createRestaurant(RestaurantInput restaurantInput, String ownerId) {
        OwnerDocument owner =
                ownerRepository.findById(ownerId).orElseThrow(() -> new AppException(ErrorCode.VENDOR_NOT_EXISTED));
        RestaurantDocument restaurant = restaurantMapper.toRestaurantDocument(restaurantInput);
        restaurant.setOwner(owner);
        if (restaurantInput.getLocation() != null
                && restaurantInput.getLocation().getCoordinates() != null) {
            var coords = restaurantInput.getLocation().getCoordinates();
            restaurant.setLocation(new GeoJsonPoint(coords.get(0), coords.get(1)));
        }
        restaurant.setUniqueRestaurantId(UniqueIdUtil.generateRestaurantUniqueId(owner.getUniqueId()));

        String slug = setSlug(restaurant.getName());
        restaurant.setSlug(slug);
        Long nextId = restaurantRepository.count() + 1;
        restaurant.setOrderId(nextId);
        restaurant.setOrderPrefix(UniqueIdUtil.generateOrderPrefix());
        restaurant.setTax(restaurantInput.getSalesTax());
        restaurant.setOrderId(1L);
        restaurant.setIsAvailable(true);
        restaurant.setActive(restaurantInput.isActive());
        restaurant.setSections(List.of("66b44629329c70266a0269d2"));
        restaurant.setTags(restaurantTagService.generateBasicTags(restaurant));
        restaurant.setKeywords(restaurantTagService.generateBasicKeywords(restaurant));
        restaurant = restaurantRepository.save(restaurant); // chỉ cần lưu restaurant thôi
        // add them nha hang vao vendor
        owner.getRestaurants().add(restaurant);
        // them restaurant vao bang ownerDocument
        OwnerDocument restaurantOwnerLogin = OwnerDocument.builder()
                .image(restaurant.getImage())
                .name(restaurant.getName())
                .email(restaurant.getUsername())
                .password(passwordEncoder.encode(restaurant.getPassword()))
                .plainPassword(restaurant.getPassword())
                .userTypeId(restaurant.getId())
                .userType(UserTypeConstant.RESTAURANT)
                .build();
        ownerRepository.save(restaurantOwnerLogin);
        ownerRepository.save(owner);
        return restaurant;
    }

    public String setSlug(String name) {
        if (name != null) {
            // Bỏ dấu tiếng Việt (normalize Unicode)
            String slug = Normalizer.normalize(name, Normalizer.Form.NFD);
            slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            // Chuyển sang lowercase và loại bỏ ký tự đặc biệt
            slug = slug.trim().toLowerCase();
            slug = slug.replaceAll("[^a-z0-9\\s-]", ""); // chỉ giữ chữ, số, khoảng trắng, gạch
            slug = slug.replaceAll("\\s+", "-"); // thay khoảng trắng thành gạch ngang
            slug = slug.replaceAll("-{2,}", "-"); // gộp gạch ngang liên tiếp thành 1
            slug = slug.replaceAll("^-|-$", ""); // bỏ gạch ở đầu/cuối

            return slug;
        }
        return null;
    }

    @Override
    public List<RestaurantDocument> getAllRestaurants(PageRequest pageable) {
        return restaurantRepository.findAll(pageable).getContent();
    }

    @Override
    public Long count() {
        return restaurantRepository.count();
    }

    @Override
    public UpdateRestaurantResponse updateDeliveryBoundsAndLocation(UpdateDeliveryBoundsInput input) {
        RestaurantDocument restaurant = restaurantRepository
                .findById(input.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        List<ZoneDocument> activeZones = zoneService.findByIsActive(true);
        boolean isInsideZone = false;
        GeoJsonPoint inputLocation = null;
        if (input.getLocation() != null) {
            inputLocation = new GeoJsonPoint(
                    input.getLocation().getLongitude(), input.getLocation().getLatitude());
        }
        if (inputLocation != null) {
            for (ZoneDocument zone : activeZones) {
                GeoJsonPolygon zonePolygon = zone.getLocation2();
                if (zonePolygon != null && isPointInsidePolygon(inputLocation, zonePolygon)) {
                    isInsideZone = true;
                    restaurant.setZone(zone);
                    break;
                }
            }
        }

        if (!isInsideZone) {
            throw new AppException(ErrorCode.LOCATION_OUTSIDE_ZONE);
        }
        switch (input.getBoundType()) {
            case DeliveryConstant.POINT -> {
                restaurant.setDeliveryBounds(null);
            }
            case DeliveryConstant.POLYGON, DeliveryConstant.RADIUS -> {
                // ✅ Convert bounds to GeoJsonPolygon
                List<org.springframework.data.geo.Point> points = input.getBounds().getFirst().stream()
                        .map(coord -> new org.springframework.data.geo.Point(coord.getFirst(), coord.get(1)))
                        .collect(Collectors.toList());
                restaurant.setDeliveryBounds(new GeoJsonPolygon(points));
            }
            default -> {
                throw new AppException(ErrorCode.BOUND_TYPE_NOT_VALID);
            }
        }

        // ✅ Convert location
        if (input.getLocation() != null) {
            restaurant.setLocation(new GeoJsonPoint(
                    input.getLocation().getLongitude(), input.getLocation().getLatitude()));
        }
        if (!input.getAddress().isEmpty()) {
            restaurant.setAddress(input.getAddress());
        }
        restaurant.setBoundType(input.getBoundType());
        restaurant.setPostCode(input.getPostCode());
        restaurant.setCity(input.getCity());
        restaurant.setCircleBounds(input.getCircleBounds());

        RestaurantDocument saved = restaurantRepository.save(restaurant);

        // ✅ Trả về dạng response chuẩn
        return UpdateRestaurantResponse.builder().success(true).data(saved).build();
    }

    @Override
    public RestaurantDocument findById(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }

    @Override
    public RestaurantDocument getRestaurantDeliveryZoneInfo(String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
    }

    @Override
    public RestaurantDocument updateTimings(String id, List<TimingsInput> openingTimes) {
        RestaurantDocument restaurant =
                restaurantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        List<OpeningTimes> openingTimesRestaurant = new ArrayList<>();

        openingTimes.forEach(openingTime -> {
            List<Timings> timings = openingTime.getTimes().stream()
                    .map(timeInput -> {
                        Timings timing = new Timings();
                        timing.setStartTime(timeInput.getStartTime());
                        timing.setEndTime(timeInput.getEndTime());
                        return timing;
                    })
                    .toList();
            OpeningTimes openingTimeRestaurant = new OpeningTimes();
            openingTimeRestaurant.setTimes(timings);
            openingTimeRestaurant.setDay(openingTime.getDay());
            openingTimesRestaurant.add(openingTimeRestaurant);
        });
        restaurant.setOpeningTimes(openingTimesRestaurant);

        return restaurantRepository.save(restaurant);
    }

    @Override
    public RestaurantDocument editRestaurant(RestaurantProfileInput input) {
        RestaurantDocument restaurant = restaurantRepository
                .findById(input.getId())
                .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        restaurantMapper.updateRestaurant(input, restaurant);
        if (input.getPassword() != null || input.getUsername() != null) {
            OwnerDocument ownerDocument = ownerRepository
                    .findByUserTypeId(input.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
            ownerDocument.setPassword(passwordEncoder.encode(input.getPassword()));
            ownerDocument.setPlainPassword(input.getPassword());
            ownerDocument.setEmail(input.getUsername());
            ownerRepository.save(ownerDocument);
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public UpdateRestaurantResponse updateRestaurantBussinessDetails(String id, BussinessDetailsInput input) {
        RestaurantDocument restaurant =
                restaurantRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));

        // Gán thông tin business mới
        BussinessDetails bussinessDetails = new BussinessDetails();
        bussinessDetails.setBankName(input.getBankName());
        bussinessDetails.setAccountName(input.getAccountName());
        bussinessDetails.setAccountCode(input.getAccountCode());
        bussinessDetails.setAccountNumber(input.getAccountNumber());
        bussinessDetails.setBussinessRegNo(input.getBussinessRegNo());
        bussinessDetails.setCompanyRegNo(input.getCompanyRegNo());
        bussinessDetails.setTaxRate(input.getTaxRate());
        restaurant.setBussinessDetails(bussinessDetails);

        RestaurantDocument updated = restaurantRepository.save(restaurant);

        return UpdateRestaurantResponse.builder()
                .success(true)
                .message("Cập nhật thông tin kinh doanh thành công")
                .data(updated)
                .build();
    }

    @Override
    public List<RestaurantDocument> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<RestaurantDocument> findNearbyRestaurants(double longitude, double latitude, double radiusKm) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(radiusKm, Metrics.KILOMETERS);
        return restaurantRepository.findByLocationNear(point, distance);

    }

    @Override
    public List<RestaurantPreview> findNearByLocation(double longitude, double latitude, String shopType) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(100, Metrics.KILOMETERS);
        List<RestaurantDocument> restaurantDocuments = restaurantRepository.findByLocationNear(point, distance);
        if (shopType != null) {
            restaurantDocuments = restaurantDocuments.stream()
                    .filter(restaurantDocument -> restaurantDocument.getShopType().equals(shopType))
                    .toList();
        }
        return restaurantDocuments.stream().map(
                restaurantDocument -> {
                    RestaurantPreview restaurantPreview = restaurantMapper.toRestaurantPreview(restaurantDocument);
                    restaurantPreview.setIsAvailable(true);
                    return restaurantPreview;
                }
        ).toList();
    }

    @Override
    public List<RestaurantPreview> getMostOrderedRestaurants(Double latitude, Double longitude) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(100, Metrics.KILOMETERS);
        List<RestaurantDocument> restaurantDocuments = restaurantRepository.findByLocationNear(point, distance);

        return restaurantDocuments.stream().map(
                restaurantDocument -> {
                    RestaurantPreview restaurantPreview = restaurantMapper.toRestaurantPreview(restaurantDocument);
                    restaurantPreview.setIsAvailable(true);
                    return restaurantPreview;
                }
        ).toList();
    }

    @Override
    public AuthData login(String username, String password) {
        RestaurantDocument user =
                restaurantRepository.findByUsername(username).orElseThrow(
                        () -> new AppException(ErrorCode.RESTAURANT_NOT_EXISTED));
        boolean authenticated = password.equals(user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHETICATED);
        }

        var token = generateToken(user);

        return AuthData.builder()
                .restaurantId(user.getId())
                .token(token)
                .build();
    }


    private boolean isPointInsidePolygon(GeoJsonPoint point, GeoJsonPolygon polygon) {
        double x = point.getX();
        double y = point.getY();

        List<Point> points = polygon.getPoints();
        boolean inside = false;

        for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            double xi = points.get(i).getX(), yi = points.get(i).getY();
            double xj = points.get(j).getX(), yj = points.get(j).getY();

            boolean intersect = ((yi > y) != (yj > y)) && (x < (xj - xi) * (y - yi) / (yj - yi + 0.0) + xi);
            if (intersect) inside = !inside;
        }

        return inside;
    }

//    private void generateTagsAndKeywords(RestaurantDocument restaurant){
//        Set<String> tags = new LinkedHashSet<>();
//        Set<String> keywords = new LinkedHashSet<>();
//        // 1️⃣ Thêm tên nhà hàng vào keywords
//        if (restaurant.getName() != null) {
//            keywords.add(restaurant.getName());
//        }
//        // 2️⃣ Lặp qua toàn bộ categories
//        for (CategoryDocument category : restaurant.getCategories()) {
//
//            // TAG = category title
//            if (category.getTitle() != null) {
//                tags.add(category.getTitle());
//                keywords.add(category.getTitle());
//            }
//
//            // 3️⃣ SubCategories → keywords
//            if (category.getSubCategories() != null) {
//                category.getSubCategories().forEach(sub -> {
//                    if (sub.getTitle() != null) {
//                        keywords.add(sub.getTitle());
//                    }
//                });
//            }
//
//            // 4️⃣ Foods → keywords
//            if (category.getFoods() != null) {
//                category.getFoods().forEach(food -> {
//
//                    if (food.getTitle() != null) {
//                        keywords.add(food.getTitle());
//                    }
//
//                    // 5️⃣ Variations → keywords
//                    if (food.getVariations() != null) {
//                        food.getVariations().forEach(variation -> {
//                            if (variation.getTitle() != null) {
//                                keywords.add(variation.getTitle());
//                            }
//                        });
//                    }
//                });
//            }
//        }
//        // 6️⃣ Addons → keywords
//        if (restaurant.getAddons() != null) {
//            restaurant.getAddons().forEach(addon -> {
//                if (addon.getTitle() != null) {
//                    keywords.add(addon.getTitle());
//                }
//            });
//        }
//        // 7️⃣ Options → keywords
//        if (restaurant.getOptions() != null) {
//            restaurant.getOptions().forEach(opt -> keywords.add(opt.getTitle()));
//        }
//        // 8️⃣ Gán về document
//        restaurant.setTags(new ArrayList<>(tags));
//        restaurant.setKeywords(new ArrayList<>(keywords));
//    }



    private String generateToken(RestaurantDocument user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("deliveryApp.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim(
                        "refreshToken",
                        new Date(Instant.now()
                                .plus(REFRESH_DURATION, ChronoUnit.SECONDS)
                                .toEpochMilli()))
                .claim("scope", "ROLE_" + "STORE")
                .claim("restaurantId", user.getId())
                .claim("email",user.getUsername())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}