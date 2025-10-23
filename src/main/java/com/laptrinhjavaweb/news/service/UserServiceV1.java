package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.UserDocument;
import com.laptrinhjavaweb.news.repository.mongo.UserV1Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceV1 {
    private final UserV1Repository userRepository;

    public UserDocument createUser(UserDocument user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(user);
    }
}
