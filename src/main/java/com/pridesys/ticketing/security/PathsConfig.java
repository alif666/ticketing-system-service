package com.pridesys.ticketing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {
    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/api/auth/**",
                "/api/health",
                "/actuator/health",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        );
    }

    @Bean(name = "appAdminPaths")
    public List<String> appAdminPaths() {
        return List.of("/api/clients/**");
    }

    @Bean(name = "clientAdminPaths")
    public List<String> clientAdminPaths() {
        return List.of(
                "/api/users/**",
                "/api/projects/*/modules/**",
                "/api/issues/verification-queue",
                "/api/issues/*/verification/approve",
                "/api/issues/*/verification/reject"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of("/api/me/**", "/api/projects/**", "/api/issues/**");
    }
}
