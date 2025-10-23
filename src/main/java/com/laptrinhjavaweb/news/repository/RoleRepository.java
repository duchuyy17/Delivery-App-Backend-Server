package com.laptrinhjavaweb.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.news.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByCode(String name);
}
