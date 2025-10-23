package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;
import com.laptrinhjavaweb.news.repository.mongo.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    @Override
    public ConfigurationDocument getConfiguration() {
        return configurationRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public ConfigurationDocument save(ConfigurationDocument config) {
        return configurationRepository.save(config);
    }
}
