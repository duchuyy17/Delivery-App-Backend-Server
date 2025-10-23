package com.laptrinhjavaweb.news.config;

import java.util.HashSet;

import com.laptrinhjavaweb.news.mongo.OwnerDocument;
import com.laptrinhjavaweb.news.mongo.RoleDocument;
import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.OwnerRepository;
import com.laptrinhjavaweb.news.repository.mongo.RoleV1Repository;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.laptrinhjavaweb.news.entity.RoleEntity;
import com.laptrinhjavaweb.news.entity.UserEntity;
import com.laptrinhjavaweb.news.repository.RoleRepository;
import com.laptrinhjavaweb.news.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(OwnerRepository userRepository, RoleV1Repository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                OwnerDocument user = OwnerDocument.builder()
                        .name("admin")
                        .password(passwordEncoder.encode("123123"))
                        .email("admin@gmail.com")
                        .userType("ADMIN")
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}
