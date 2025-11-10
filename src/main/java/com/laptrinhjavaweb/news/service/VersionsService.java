package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.data.VersionsDTO;
import org.springframework.stereotype.Service;

@Service
public class VersionsService {
    public VersionsDTO getVersions() {
        // Fake data
        VersionsDTO.AppVersionDTO appVersion = new VersionsDTO.AppVersionDTO("1.0.7", "1.0.6");
        return new VersionsDTO(appVersion);
    }
}
