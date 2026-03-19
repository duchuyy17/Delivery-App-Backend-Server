package com.laptrinhjavaweb.news.service;

import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.dto.data.VersionsDTO;

@Service
public class VersionsService {
    public VersionsDTO getVersions() {
        // Fake data
        VersionsDTO.AppVersionDTO appVersion = new VersionsDTO.AppVersionDTO("1.0.7", "1.0.6");
        return new VersionsDTO(appVersion);
    }
}
