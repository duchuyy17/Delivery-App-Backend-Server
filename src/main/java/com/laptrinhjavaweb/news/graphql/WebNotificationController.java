package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.mongo.WebNotification;
import com.laptrinhjavaweb.news.service.WebNotificationService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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
