package com.laptrinhjavaweb.news.constant;

public class SystemConstant {
    public static final String GEO_KEY = "riders:online:geo"; // vị trí rider -> tìm rider gần nhất
    public static final String RIDER_LAST_UPDATE = "riders:last:update"; // xét rider online
    public static final String BUSY_KEY = "riders:online:busy"; // rider đang nhận đơn
    public static final String ORDER_DISPATCH_KEY = "order_dispatched:"; //  // mark rider đã được offer order nào
    public static final String RETRY_KEY = "order:retry:delay"; // order chờ retry theo time
    public static final String PENDING_OFFER_KEY =
            "pending_offer"; // order đang được offer kiểm tra timout -> retry rider khác
    public static final String TRAFFIC_ZONE_KEY = "traffic:zone:";
    public static final String PICKUP = "PICKUP";
    public static final String RETRY_COUNT_KEY = "order:retry:count:";
    public static final int MAX_RETRY = 3;
    public static final String DELIVERY = "DELIVERY";
}
