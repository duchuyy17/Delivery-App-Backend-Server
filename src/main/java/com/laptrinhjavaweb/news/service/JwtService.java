package com.laptrinhjavaweb.news.service;


import java.text.ParseException;

public interface JwtService {
    String getCurrentUserId() throws ParseException;
}
