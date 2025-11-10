package com.laptrinhjavaweb.news.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.laptrinhjavaweb.news.service.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadImageCloudinary(MultipartFile file) throws IOException {
        String publicValue = generatePublicValue(file.getOriginalFilename());
        log.info("originalFileName is: {}", file.getOriginalFilename());
        log.info("publicValue is: {}", publicValue);
        String extension = getFileName(Objects.requireNonNull(file.getOriginalFilename()))[1];
        log.info("extension is: {}", extension);
        File fileUpload = convert(file);
        log.info("fileUpload is: {}", fileUpload);
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
        cleanDisk(fileUpload);
        return cloudinary.url().secure(true).generate(StringUtils.join(publicValue, ".", extension));
    }

    @Override
    public String uploadImageBase64(String base64Data) throws IOException {
        // Kiểm tra tiền tố để xác định loại file
        String resourceType = "auto"; // Cloudinary tự nhận diện (image/video/raw)
        if (base64Data.startsWith("data:image")) {
            resourceType = "image";
        } else if (base64Data.startsWith("data:video")) {
            resourceType = "video";
        }

        // Loại bỏ phần tiền tố "data:xxx;base64,"
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1];
        }

        // Giải mã Base64 thành byte[]
        byte[] fileBytes = Base64.getDecoder().decode(base64Data);

        // Sinh public_id ngẫu nhiên
        String publicValue = generatePublicValue(UUID.randomUUID().toString());
        log.info("Uploading Base64 {} with public ID: {}", resourceType, publicValue);

        // Upload lên Cloudinary
        Map uploadResult = cloudinary
                .uploader()
                .upload(fileBytes, ObjectUtils.asMap(
                        "resource_type", resourceType, // có thể là "image" hoặc "video"
                        "public_id", publicValue
                ));

        // Lấy URL trả về
        return uploadResult.get("secure_url").toString();
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(
                generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            log.info("file.toPath(): {}", file.toPath());
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            log.error("Error");
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}
