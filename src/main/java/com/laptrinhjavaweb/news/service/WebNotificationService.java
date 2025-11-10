package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.mongo.WebNotification;

public interface WebNotificationService {
    List<WebNotification> getAllNotifications();
}
