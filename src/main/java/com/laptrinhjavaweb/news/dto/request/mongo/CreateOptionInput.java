package com.laptrinhjavaweb.news.dto.request.mongo;

import lombok.Data;

import java.util.List;

@Data
public class CreateOptionInput {
    private String restaurant;
    private List<OptionInput> options;
}
