package com.backbase.dbs.capabilities.extended;

import com.backbase.buildingblocks.backend.configuration.autoconfigure.BackbaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The main Application class for Persistence service.
 */
@BackbaseApplication
@EnableAutoConfiguration
@EnableHystrix
@EnableJpaRepositories(basePackages = {"com.backbase.buildingblocks.persistence.repository", "com.backbase.dbs.capabilities.extended.repository"})
@EntityScan(basePackages = {"com.backbase.buildingblocks.persistence.model", "com.backbase.dbs.capabilities.extended.domain"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
