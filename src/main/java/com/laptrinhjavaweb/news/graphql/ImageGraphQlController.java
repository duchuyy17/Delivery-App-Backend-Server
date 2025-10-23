package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.response.mongo.ImageResponse;
import com.laptrinhjavaweb.news.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ImageGraphQlController {
    private final ImageService imageService;

    @MutationMapping
    public ImageResponse uploadImageToS3(@Argument String image) throws Exception {
        String imageUrl = imageService.uploadImageBase64(image);
        return new ImageResponse(imageUrl);
    }
}
