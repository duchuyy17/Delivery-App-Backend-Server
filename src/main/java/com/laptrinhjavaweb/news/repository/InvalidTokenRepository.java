package com.laptrinhjavaweb.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laptrinhjavaweb.news.entity.InvalidToken;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {}
