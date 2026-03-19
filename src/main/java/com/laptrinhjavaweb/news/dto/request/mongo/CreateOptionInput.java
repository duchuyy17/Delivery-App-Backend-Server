package com.laptrinhjavaweb.news.dto.request.mongo;

import java.util.List;

import lombok.Data;

@Data
public class CreateOptionInput {
    private String restaurant;
    private List<OptionInput> options;
}
