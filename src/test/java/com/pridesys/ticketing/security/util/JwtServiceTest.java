package com.pridesys.ticketing.security.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import com.pridesys.ticketing.entity.UserEntity;
import com.pridesys.ticketing.entity.UserRole;

class JwtServiceTest {
    private final JwtService service = new JwtService("test-secret-that-is-at-least-32-characters", java.time.Duration.ofHours(1));

    @Test
    void issuedTokenRoundTripsIdentityAndRole() {
        UserEntity user = new UserEntity("user@example.com", "hash", UserRole.CLIENT_ADMIN, "User", null);
        user.setId(4L);
        JwtService.UserPrincipal principal = service.parse(service.issue(user));
        assertThat(principal.id()).isEqualTo(4);
        assertThat(principal.email()).isEqualTo("user@example.com");
        assertThat(principal.role()).isEqualTo(UserRole.CLIENT_ADMIN);
    }
}
