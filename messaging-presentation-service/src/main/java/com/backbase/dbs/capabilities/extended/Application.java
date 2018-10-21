package com.backbase.dbs.capabilities.extended;

import com.backbase.buildingblocks.backend.configuration.autoconfigure.BackbaseApplication;
import com.backbase.buildingblocks.jwt.internal.config.EnableInternalJwtConsumer;
import com.backbase.buildingblocks.registry.client.api.EnableRegistryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@BackbaseApplication
@EnableInternalJwtConsumer
@EnableRegistryClient
public class Application extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
