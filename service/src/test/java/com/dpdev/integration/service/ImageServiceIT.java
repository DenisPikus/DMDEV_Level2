package com.dpdev.integration.service;

import com.dpdev.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "app.image.bucket=/Users/Denis/maven-homework/images/"
})
@RequiredArgsConstructor
public class ImageServiceIT {

    private final ImageService imageService;

    @Test
    void uploadAndGetImageTest() throws IOException {
        String imagePath = "test-image.png";
        String content = "Test image content";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        MockMultipartFile imageFile = new MockMultipartFile("image", imagePath, MediaType.APPLICATION_OCTET_STREAM_VALUE, inputStream);

        imageService.upload(imagePath, imageFile.getInputStream());

        Optional<byte[]> actualResult = imageService.get(imagePath);

        assertTrue(actualResult.isPresent());
        assertArrayEquals(content.getBytes(StandardCharsets.UTF_8), actualResult.get());
    }

    @Test
    void getIfImageNotExistTest() {
        Optional<byte[]> retrievedImage = imageService.get("dummy.png");

        assertFalse(retrievedImage.isPresent());
    }
}
