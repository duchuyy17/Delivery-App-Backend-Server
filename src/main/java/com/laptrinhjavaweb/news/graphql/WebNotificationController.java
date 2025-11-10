package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.mongo.WebNotification;
import com.laptrinhjavaweb.news.service.WebNotificationService;

@Controller
public class WebNotificationController {
    private final WebNotificationService webNotificationService;

    public WebNotificationController(WebNotificationService webNotificationService) {
        this.webNotificationService = webNotificationService;
    }

    @QueryMapping
    public List<WebNotification> webNotifications() {
        return webNotificationService.getAllNotifications();
    }
}
