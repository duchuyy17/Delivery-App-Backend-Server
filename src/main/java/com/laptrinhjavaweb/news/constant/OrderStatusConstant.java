package com.laptrinhjavaweb.news.constant;

import java.util.List;
import java.util.Map;

public class OrderStatusConstant {
    public static final String ACCEPTED = "ACCEPTED";
    public static final String REJECTED = "REJECTED";
    public static final String PENDING = "PENDING";
    public static final String DELIVERED = "DELIVERED";
    public static final String ASSIGNED = "ASSIGNED";
    public static final String PICKED = "PICKED";
    public static final String CANCELLED = "CANCELLED";
    public static final String WAITINGRIDER = "WAITING RIDER";

    public static final Map<String, List<String>> VALID_TRANSITIONS = Map.of(
            PENDING, List.of(REJECTED, ACCEPTED),
            ACCEPTED, List.of(ASSIGNED),
            ASSIGNED, List.of(PICKED),
            PICKED, List.of(DELIVERED));
}
