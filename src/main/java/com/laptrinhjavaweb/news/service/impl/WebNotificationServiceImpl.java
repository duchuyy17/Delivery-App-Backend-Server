package com.laptrinhjavaweb.news.service.impl;

import java.util.List;

import com.laptrinhjavaweb.news.service.WebNotificationService;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.mongo.WebNotification;
import com.laptrinhjavaweb.news.repository.mongo.WebNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebNotificationServiceImpl implements WebNotificationService {
    private final WebNotificationRepository repository;

    @Override
    public List<WebNotification> getAllNotifications() {
        return repository.findAll();
    }
}
