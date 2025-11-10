package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.service.ConfigurationService;
import org.springframework.stereotype.Service;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;
import com.laptrinhjavaweb.news.repository.mongo.ConfigurationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    @Override
    public ConfigurationDocument getConfiguration() {
        return configurationRepository.findAll().stream().findFirst().orElse(null);
    }

    @Override
    public ConfigurationDocument save(ConfigurationDocument config) {
        return configurationRepository.save(config);
    }
}
