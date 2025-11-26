package com.laptrinhjavaweb.news.service;

import com.laptrinhjavaweb.news.dto.request.mongo.TipInput;
import com.laptrinhjavaweb.news.mongo.TipDocument;

import java.util.List;

public interface TipService {
    List<TipDocument> getAllTips();
    TipDocument createTip(TipDocument tip);
    TipDocument editTipping(TipInput input);
}
