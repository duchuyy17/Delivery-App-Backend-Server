package com.laptrinhjavaweb.news.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.data.Otp;
import com.laptrinhjavaweb.news.dto.data.VerifyOtp;
import com.laptrinhjavaweb.news.service.impl.EmailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmailGraphQLController {
    private final EmailService emailService;

    @MutationMapping
    public Otp sendOtpToEmail(@Argument String email) {
        return emailService.sendOtpToEmail(email);
    }

    @MutationMapping
    public VerifyOtp verifyOtp(@Argument("otp") String otp, @Argument("email") String email, @Argument String phone) {
        return emailService.verifyOtp(email, otp, phone);
    }
}
