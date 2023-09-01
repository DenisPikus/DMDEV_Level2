package com.dpdev.integration.http.rest;

import com.dpdev.integration.IntegrationTestBase;
import com.dpdev.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@RequiredArgsConstructor
public class UserRestControllerIT extends IntegrationTestBase {

    public static final String IMAGE_PATH = "avatar_1.png";
    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER_ID = -1L;

    private final MockMvc mockMvc;
    private final ImageService imageService;

    @Value("${app.image.bucket}")
    private final String bucket;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json((getExpectedJsonContentFromFile("json/users.json"))));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json((getExpectedJsonContentFromFile("json/user.json"))));
    }

    @Test
    public void findByIdWhenUserIsNotPresent() throws Exception {
        mockMvc.perform(get("/api/v1/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Path fullImagePath = Path.of(bucket, IMAGE_PATH);
        byte[] imageBytes = Files.readAllBytes(fullImagePath);
        MockMultipartFile multipartFile =
                new MockMultipartFile("image", "avatar_1.png", MediaType.MULTIPART_FORM_DATA_VALUE, imageBytes);

        ResultActions request = mockMvc.perform(multipart("/api/v1/users")
                    .file(multipartFile)
                .param("firstname", "Test")
                .param("lastname", "Test")
                .param("email", "test@gmail.com")
                .param("phoneNumber", "3751234567822")
                .param("address", "BY, Gomel, 123 Sovetskaja St")
                .param("role", "ADMIN")
                .param("image", "avatar_1.png")
        );
        request
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstname").value("Test"))
                .andExpect(jsonPath("$.lastname").value("Test"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("3751234567822"))
                .andExpect(jsonPath("$.address").value("BY, Gomel, 123 Sovetskaja St"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.image").value("avatar_1.png"));
    }

    @Test
    void update() throws Exception {
        Path imagePath = Paths.get(IMAGE_PATH);
        byte[] imageBytes = Files.readAllBytes(imagePath);
        MockMultipartFile imageFile = new MockMultipartFile("avatar_1", "avatar_1.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, imageBytes);

        mockMvc.perform(multipart("/api/v1/users/{id}", USER_ID)
                        .file(imageFile)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getExpectedJsonContentFromFile("/json/update-user.json"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.firstname").value("Test"),
                        jsonPath("$.lastname").value("Test"),
                        jsonPath("$.email").value("test@gmail.com"),
                        jsonPath("$.phoneNumber").value("3751234567890"),
                        jsonPath("$.address").value("BY, Gomel, 123 Sovetskaja St"),
                        jsonPath("$.role").value("ADMIN"),
                        jsonPath("$.image").value("avatar_2.png")
                );
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", USER_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", INVALID_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAvatar() throws Exception {
        Optional<byte[]> avatarContent = imageService.get(IMAGE_PATH);

        MockMultipartFile avatarFile = new MockMultipartFile("avatar", "avatar_1.png", MediaType.APPLICATION_OCTET_STREAM_VALUE, avatarContent.get());

        MockHttpServletResponse response = mockMvc.perform(multipart("/{id}/avatar", USER_ID)
                        .file(avatarFile))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertArrayEquals(avatarContent.get(), response.getContentAsByteArray());
    }
}
