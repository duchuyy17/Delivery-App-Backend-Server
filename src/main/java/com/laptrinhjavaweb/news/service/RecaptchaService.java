package com.laptrinhjavaweb.news.service;

public interface RecaptchaService {
    boolean verify(String token);
}
