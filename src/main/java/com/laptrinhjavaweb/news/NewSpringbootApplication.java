package com.laptrinhjavaweb.news;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@EntityScan(basePackages = "com.laptrinhjavaweb.news.entity")
@EnableMongoRepositories(basePackages = "com.laptrinhjavaweb.news.repository.mongo")
public class NewSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewSpringbootApplication.class, args);
    }
}
