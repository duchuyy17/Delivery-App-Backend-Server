package com.laptrinhjavaweb.news.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHETICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNFIND_ROLE(1007, "role cound not find", HttpStatus.NOT_FOUND),

    ACCESS_DENIED(1008, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_DAYOFBIRTH(1009, "your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_FORMAT(
            1010,
            "Password must contain at least one uppercase letter and one special character",
            HttpStatus.BAD_REQUEST),
    INVALID_PHONENUMBER(1011, "phone number must start with 0 and extract with 10 number", HttpStatus.BAD_REQUEST),
    TOKEN_ALREADY_EXPIRED(1012, "token already expired", HttpStatus.BAD_REQUEST),
    VENDOR_NOT_EXISTED(1013,"Vendor not existed",HttpStatus.NOT_FOUND),
    ZONE_NOT_EXISTED(1014,"Zone not existed",HttpStatus.NOT_FOUND),
    RESTAURANT_NOT_EXISTED(1015,"Restaurant not found",HttpStatus.NOT_FOUND),
    LOCATION_OUTSIDE_ZONE(1016,"Địa điểm cửa hàng không thuộc phạm vi giao hàng",HttpStatus.BAD_REQUEST),
    BOUND_TYPE_NOT_VALID(1017,"Kiểu vùng không hợp lệ",HttpStatus.BAD_REQUEST),
    ZONE_NOT_FOUND (1018,"Phạm vi giao hàng không tồn tại!",HttpStatus.BAD_REQUEST),
    RIDER_NOT_FOUND(1019,"Không tìm thấy tài xế!",HttpStatus.NOT_FOUND);
    ErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode status;
}
