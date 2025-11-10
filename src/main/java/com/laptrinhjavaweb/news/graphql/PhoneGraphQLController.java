package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.data.Otp;
import com.laptrinhjavaweb.news.service.impl.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PhoneGraphQLController {
    private final OtpService otpService;

    @MutationMapping
    public Otp sendOtpToPhoneNumber(@Argument String phone) {
        return otpService.sendOtpToPhoneNumber(phone);
    }
}
