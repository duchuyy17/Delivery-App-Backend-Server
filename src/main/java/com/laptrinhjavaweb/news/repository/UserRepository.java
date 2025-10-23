package com.laptrinhjavaweb.news.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.news.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(long id);

    boolean existsByUserName(String username);

    Page<UserEntity> findByUserName(String username, Pageable pageable);

    Optional<UserEntity> findByUserName(String username);
}
