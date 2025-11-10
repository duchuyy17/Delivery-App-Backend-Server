package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.data.Otp;
import com.laptrinhjavaweb.news.dto.data.VerifyOtp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Otp sendOtpToEmail(String email) {
        try {
            // Sinh OTP ngẫu nhiên
            String otp = String.format("%06d", new Random().nextInt(999999));

            // Gửi email
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Mã OTP xác thực của bạn");
            message.setText("Mã OTP của bạn là: " + otp + "\nCó hiệu lực trong 3 phút.");
            mailSender.send(message);

            // Lưu OTP vào Redis với TTL = 3 phút
            redisTemplate.opsForValue().set("OTP:" + email, otp, 3, TimeUnit.MINUTES);

            System.out.println("✅ OTP sent and cached for " + email);
            return Otp.builder().result(true).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Otp.builder().result(false).build();
        }
    }

    public VerifyOtp verifyOtp(String email, String otp, String phone) {
        String key = null;

        // Ưu tiên xác định key theo thông tin đầu vào
        if (email != null && !email.isEmpty()) {
            key = "OTP:email:" + email;
        } else if (phone != null && !phone.isEmpty()) {
            key = "OTP:phone:" + phone;
        } else {
            // Nếu không có cả email lẫn phone → sai yêu cầu
            return VerifyOtp.builder().result(false).build();
        }

        // Lấy OTP đã lưu trong Redis
        Object cachedOtp = redisTemplate.opsForValue().get(key);

        if (cachedOtp != null && cachedOtp.equals(otp)) {
            // Xóa sau khi xác thực thành công
            redisTemplate.delete(key);
            System.out.println("✅ OTP verified successfully for key: " + key);
            return VerifyOtp.builder().result(true).build();
        }

        System.out.println("❌ OTP verification failed for key: " + key);
        return VerifyOtp.builder().result(false).build();
    }

}
