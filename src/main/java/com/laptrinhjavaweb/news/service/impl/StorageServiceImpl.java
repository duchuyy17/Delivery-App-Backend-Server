package com.laptrinhjavaweb.news.service.impl;

import java.io.IOException;
import java.util.Optional;

import com.laptrinhjavaweb.news.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.laptrinhjavaweb.news.entity.ImageData;
import com.laptrinhjavaweb.news.repository.StorageRepository;
import com.laptrinhjavaweb.news.util.ImageUtil;

@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    private StorageRepository storageRepository;

    @Override
    public String upload(MultipartFile file) throws IOException {
        ImageData imageData = storageRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .build());
        if (imageData != null) {
            return "file upload success:" + file.getOriginalFilename();
        }
        return null;
    }

    @Override
    public byte[] download(String fileName) {
        Optional<ImageData> imageData = storageRepository.findByName(fileName);
        byte[] images = ImageUtil.decompressImage(imageData.get().getImageData());
        return images;
    }
}
