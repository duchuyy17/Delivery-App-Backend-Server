package com.laptrinhjavaweb.news.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadImageCloudinary(MultipartFile file) throws IOException;
    String uploadImageBase64(String base64Data) throws IOException;
}
