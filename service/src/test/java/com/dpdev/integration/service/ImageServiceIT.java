package com.dpdev.integration.service;

import com.dpdev.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor
public class ImageServiceIT {

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.image.bucket", () -> System.getProperty("java.io.tmpdir"));
    }

    @Value("${app.image.bucket}")
    private final String bucket;

    private final ImageService imageService;

    @Test
    void uploadAndGetImage() throws IOException {
        String imagePath = "test-image.png";
        Path absolutePath = Path.of(bucket, imagePath);
        String content = "Test image content";
        Files.write(absolutePath, content.getBytes());
        File imageFile = new File(absolutePath.toString());

        imageService.upload(imagePath, new FileInputStream(imageFile));

        Optional<byte[]> actualResult = imageService.get(imagePath);

        assertTrue(actualResult.isPresent());
        assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), actualResult.get());
    }

    @Test
    void getIfImageNotExist() {
        Optional<byte[]> retrievedImage = imageService.get("dummy.png");

        assertFalse(retrievedImage.isPresent());
    }
}
