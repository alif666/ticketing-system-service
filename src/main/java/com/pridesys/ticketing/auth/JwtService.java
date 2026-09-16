package com.pridesys.ticketing.auth;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final Duration lifetime;

    public JwtService(@Value("${app.security.jwt-secret:change-this-development-secret-to-at-least-32-chars}") String secret,
                      @Value("${app.security.jwt-lifetime:PT2H}") Duration lifetime) {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) throw new IllegalArgumentException("JWT secret must be at least 32 bytes");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.lifetime = lifetime;
    }

    public String issue(UserRecord user) {
        Date now = new Date();
        return Jwts.builder().subject(Long.toString(user.id())).claim("email", user.email()).claim("role", user.role().name())
                .issuedAt(now).expiration(new Date(now.getTime() + lifetime.toMillis())).signWith(key).compact();
    }

    public UserPrincipal parse(String token) {
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return new UserPrincipal(Long.parseLong(claims.getSubject()), claims.get("email", String.class), UserRole.valueOf(claims.get("role", String.class)));
    }

    public record UserPrincipal(long id, String email, UserRole role) {}
}
