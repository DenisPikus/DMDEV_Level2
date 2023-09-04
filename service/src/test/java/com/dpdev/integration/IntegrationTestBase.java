package com.dpdev.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.dpdev.integration.util.ContainerTestUtil.postgres;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql({
        "classpath:sql/script.sql"
})
@WithMockUser(username = "user@mail.com", password = "test", authorities = {"ADMIN", "USER"})
public abstract class IntegrationTestBase {

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    public String getExpectedJsonContentFromFile(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
