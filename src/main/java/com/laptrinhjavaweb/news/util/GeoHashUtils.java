package com.laptrinhjavaweb.news.util;

import ch.hsr.geohash.GeoHash;

public class GeoHashUtils {
    public static String encode(double lat, double lng, int precision) {
        return GeoHash.geoHashStringWithCharacterPrecision(lat, lng, precision);
    }
}
