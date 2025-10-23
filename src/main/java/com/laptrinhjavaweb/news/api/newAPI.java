package com.laptrinhjavaweb.news.api;

import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.news.dto.request.NewDTO;

@RestController
public class newAPI {
    @PostMapping("/new")
    public NewDTO test(@RequestBody NewDTO newDTO) {
        return newDTO;
    }

    @GetMapping("/new")
    public String test1() {
        return "xin chao";
    }
}
