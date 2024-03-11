package ru.shadi777.proxyapplication;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;

@Testcontainers
public abstract class IntegrationEnvironment {
    @Container
    static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER;

    static Connection connection;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                .withDatabaseName("postgres_db")
                .withExposedPorts(5432, 5433);

        POSTGRE_SQL_CONTAINER.start();

    }

    @Configuration
    static class IntegrationEnvironmentConfig {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                    .username(POSTGRE_SQL_CONTAINER.getUsername())
                    .password(POSTGRE_SQL_CONTAINER.getPassword())
                    .build();
        }
    }
}
