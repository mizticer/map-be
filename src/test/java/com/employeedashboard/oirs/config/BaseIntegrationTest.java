package com.employeedashboard.oirs.config;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest(properties = {"spring.flyway.clean-disabled=false", "spring.flyway.schemas=app"})
@Transactional
public class BaseIntegrationTest {

    @ClassRule
    public static PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:15.3-alpine")
                    .withCopyFileToContainer(MountableFile.forHostPath("pg-init.sql"),
                            "/docker-entrypoint-initdb.d/master_db_init.sql")
                    .withDatabaseName("test")
                    .withUsername("root")
                    .withPassword("root");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @BeforeAll
    public static void setUpDb() {
        postgresqlContainer.start();
    }
}