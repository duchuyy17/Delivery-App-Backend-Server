package com.laptrinhjavaweb.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.news.entity.PermissionEntity;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {}
