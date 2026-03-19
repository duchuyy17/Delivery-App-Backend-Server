package com.laptrinhjavaweb.news.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.laptrinhjavaweb.news.service.StorageService;

@Service
public class StorageServiceImpl implements StorageService {

    @Override
    public String upload(MultipartFile file) throws IOException {
        //        ImageData imageData = storageRepository.save(ImageData.builder()
        //                .name(file.getOriginalFilename())
        //                .type(file.getContentType())
        //                .imageData(ImageUtil.compressImage(file.getBytes()))
        //                .build());
        //        if (imageData != null) {
        //            return "file upload success:" + file.getOriginalFilename();
        //        }
        return null;
    }

    @Override
    public byte[] download(String fileName) {
        //        Optional<ImageData> imageData = storageRepository.findByName(fileName);
        //        byte[] images = ImageUtil.decompressImage(imageData.get().getImageData());
        //        return images;
        return null;
    }
}
