package com.laptrinhjavaweb.news.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImageCloudinary(MultipartFile file) throws IOException;

    String uploadImageBase64(String base64Data) throws IOException;
}
