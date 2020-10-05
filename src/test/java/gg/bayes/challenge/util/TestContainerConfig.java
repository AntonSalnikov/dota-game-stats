package gg.bayes.challenge.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

@Slf4j
public class TestContainerConfig {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);

        private static final PostgreSQLContainer postgre = new PostgreSQLContainer<>("postgres:12.3-alpine")
                .withLogConsumer(logConsumer);

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            log.debug("Starting test containers");

            Startables.deepStart(Stream.of(postgre))
                    .join();

            log.info("Database with url {} was started", postgre.getJdbcUrl());

            TestPropertyValues.of(
                    "spring.datasource.url:" + postgre.getJdbcUrl(),
                    "spring.datasource.username:" + postgre.getUsername(),
                    "spring.datasource.password:" + postgre.getPassword()

            ).applyTo(applicationContext);
        }
    }
}
