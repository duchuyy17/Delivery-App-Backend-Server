package com.laptrinhjavaweb.news.service.impl;

import com.laptrinhjavaweb.news.dto.request.mongo.TipInput;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.mongo.TipDocument;
import com.laptrinhjavaweb.news.repository.mongo.TipRepository;
import com.laptrinhjavaweb.news.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipServiceImpl implements TipService {
    private final TipRepository tipRepository;
    @Override
    public List<TipDocument> getAllTips() {
        return tipRepository.findAll();
    }

    @Override
    public TipDocument createTip(TipDocument tip) {
        return tipRepository.save(tip);
    }

    @Override
    public TipDocument editTipping(TipInput input) {
        TipDocument tipDocument = tipRepository.findById(input.getId())
                .orElseThrow(() -> new AppException(ErrorCode.TIP_NOT_FOUND));
        tipDocument.setTipVariations(input.getTipVariations());
        tipDocument.setEnabled(input.getEnabled());
        return tipRepository.save(tipDocument);
    }
}
