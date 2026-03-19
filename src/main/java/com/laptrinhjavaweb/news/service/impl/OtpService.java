package com.laptrinhjavaweb.news.service.impl;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.Otp;
import com.laptrinhjavaweb.news.dto.data.VerifyOtp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;

    public Otp sendOtpToPhoneNumber(String phone) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        redisTemplate.opsForValue().set("otp:" + phone, otp, 2, TimeUnit.MINUTES);

        System.out.println("📱 OTP gửi tới " + phone + " là: " + otp);

        return Otp.builder().result(true).build();
    }

    public VerifyOtp verifyOtp(String phone, String otp) {
        String key = "otp:" + phone;
        String savedOtp = redisTemplate.opsForValue().get(key);

        if (savedOtp != null && savedOtp.equals(otp)) {
            // Xóa OTP khỏi Redis sau khi verify thành công
            redisTemplate.delete(key);
            System.out.println("✅ [VERIFY OTP] " + phone + " -> verified");
            return VerifyOtp.builder().result(true).build();
        }

        System.out.println("❌ [VERIFY OTP] " + phone + " -> failed");
        return VerifyOtp.builder().result(false).build();
    }
}
