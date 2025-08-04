package com.codyssey.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Spring Boot Application class for Codyssey API Server
 * <p>
 * This class serves as the entry point for the Spring Boot application.
 * It enables various Spring features like JPA auditing, caching, async processing,
 * and transaction management.
 *
 * @author Codyssey Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
public class CodysseyApplication {

    /**
     * Main method to start the Spring Boot application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CodysseyApplication.class, args);
    }
}