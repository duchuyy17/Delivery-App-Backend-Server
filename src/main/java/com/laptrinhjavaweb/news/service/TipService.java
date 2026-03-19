package com.laptrinhjavaweb.news.service;

import java.util.List;

import com.laptrinhjavaweb.news.dto.request.mongo.TipInput;
import com.laptrinhjavaweb.news.mongo.TipDocument;

public interface TipService {
    List<TipDocument> getAllTips();

    TipDocument createTip(TipDocument tip);

    TipDocument editTipping(TipInput input);
}
