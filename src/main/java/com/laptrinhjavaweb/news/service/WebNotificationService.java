package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.WebNotification;

import java.util.List;

public interface WebNotificationService {
    List<WebNotification> getAllNotifications();
}
