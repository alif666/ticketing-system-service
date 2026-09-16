package com.pridesys.ticketing.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebConfigTest {
    @Test
    void configuresFrontendOriginsAndPreflightHeaders() {
        var source = new WebConfig().corsConfigurationSource("http://localhost:5173,http://127.0.0.1:5173");
        var configuration = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest("OPTIONS", "/api/auth/login"));

        assertTrue(configuration.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(configuration.getAllowedMethods().contains("OPTIONS"));
        assertTrue(configuration.getAllowedHeaders().contains("Authorization"));
        assertEquals("http://localhost:5173", configuration.checkOrigin("http://localhost:5173"));
    }
}
