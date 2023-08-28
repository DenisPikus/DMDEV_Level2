package com.dpdev.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static com.dpdev.integration.util.ContainerTestUtil.postgres;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql({
        "classpath:sql/script.sql"
})
public abstract class IntegrationTestBase {

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }
}
