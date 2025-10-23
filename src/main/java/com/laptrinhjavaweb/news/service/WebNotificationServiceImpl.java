package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.WebNotification;
import com.laptrinhjavaweb.news.repository.mongo.WebNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WebNotificationServiceImpl implements WebNotificationService {
    private final WebNotificationRepository repository;

    @Override
    public List<WebNotification> getAllNotifications() {
        return repository.findAll();
    }
}
