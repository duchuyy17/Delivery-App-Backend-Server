package com.laptrinhjavaweb.news.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
        return cloudinary.url()
                .secure(true)
                .generate(StringUtils.join(publicValue, ".", extension));
    }

    @Override
    public String uploadImageBase64(String base64Data) throws IOException {
        // Loại bỏ tiền tố nếu có (ví dụ: data:image/png;base64,...)
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1];
        }

        // Giải mã Base64 thành mảng byte
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        // Tạo tên public ID ngẫu nhiên
        String publicValue = generatePublicValue(UUID.randomUUID().toString());
        log.info("Uploading Base64 image with public ID: {}", publicValue);

        // Upload trực tiếp lên Cloudinary bằng byte[]
        Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                "resource_type", "image",
                "public_id", publicValue
        ));

        // Lấy URL trả về
        return uploadResult.get("secure_url").toString();
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
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
