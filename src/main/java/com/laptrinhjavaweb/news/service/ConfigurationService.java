package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.mongo.ConfigurationDocument;

public interface ConfigurationService {
    ConfigurationDocument getConfiguration();
    ConfigurationDocument save(ConfigurationDocument config);
}
