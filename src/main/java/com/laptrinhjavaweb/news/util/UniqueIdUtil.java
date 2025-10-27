package com.laptrinhjavaweb.news.util;
import java.security.SecureRandom;

public class UniqueIdUtil {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private static final SecureRandom random = new SecureRandom();
        private static final int LENGTH = 5;

        public static String randomString(int length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            return sb.toString();
        }

        /**
         * Sinh mã unique cho restaurant, dựa theo uniqueId của vendor
         * @param vendorUniqueId  ví dụ: "VEN-X0KEF"
         * @return                ví dụ: "VEN-X0KEF-STR-D62BC"
         */
        public static String generateRestaurantUniqueId(String vendorUniqueId) {
            return vendorUniqueId + "-STR-" + randomString(5);
        }

        // Có thể thêm các phương thức khác, ví dụ:
        public static String generateVendorId() {
            return "VEN-" + randomString(5);
        }

    public static String generateOrderPrefix() {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
