package com.laptrinhjavaweb.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.laptrinhjavaweb.news.repository")
public class NewSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewSpringbootApplication.class, args);
    }
}
