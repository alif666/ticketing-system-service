package com.pridesys.ticketing.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PathsConfigTest {
    private final PathsConfig paths = new PathsConfig();

    @Test
    void exposesPublicAuthenticationHealthAndOpenApiPaths() {
        var configured = paths.publicPaths();
        assertTrue(configured.contains("/api/auth/**"));
        assertTrue(configured.contains("/api/health"));
        assertTrue(configured.contains("/swagger-ui/**"));
        assertTrue(configured.contains("/v3/api-docs/**"));
    }

    @Test
    void assignsAdministrativeRoutesToExpectedRoleGroups() {
        assertTrue(paths.appAdminPaths().contains("/api/clients/**"));
        assertTrue(paths.clientAdminPaths().contains("/api/users/**"));
        assertTrue(paths.clientAdminPaths().contains("/api/issues/verification-queue"));
        assertFalse(paths.clientAdminPaths().contains("/api/projects/*/modules/**"));
        assertTrue(paths.clientAdminModuleWritePaths().contains("/api/projects/*/modules/**"));
    }

    @Test
    void protectsRemainingBusinessRoutes() {
        assertTrue(paths.securedPaths().contains("/api/me/**"));
        assertTrue(paths.securedPaths().contains("/api/projects/**"));
        assertTrue(paths.securedPaths().contains("/api/issues/**"));
    }
}
