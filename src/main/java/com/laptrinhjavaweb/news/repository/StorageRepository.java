package com.laptrinhjavaweb.news.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.news.entity.ImageData;

public interface StorageRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByName(String fileName);
}
