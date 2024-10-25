package com.fpoly.be_wanren_buffet.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.concurrent.TimeUnit;

@Component
public class RestConfig implements RepositoryRestConfigurer {

    @Autowired
    private EntityManager entityManager;


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream()
                .map(entityType -> entityType.getJavaType())
                .toArray(Class[]::new));



    }




}
