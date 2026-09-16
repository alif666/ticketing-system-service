package com.pridesys.ticketing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LocalSeedData implements CommandLineRunner {
    private final JdbcTemplate jdbc;
    private final PasswordEncoder encoder;
    private final boolean enabled;

    public LocalSeedData(JdbcTemplate jdbc, PasswordEncoder encoder,
                         @Value("${app.seed.enabled:true}") boolean enabled) {
        this.jdbc = jdbc; this.encoder = encoder; this.enabled = enabled;
    }

    @Override public void run(String... args) {
        if (!enabled) return;
        String hash = encoder.encode("Password123!");
        seed("app.admin@example.com", "APP_ADMIN", "Application Admin", "01700000001", "Company Administrator", hash);
        seed("client.admin@example.com", "CLIENT_ADMIN", "Client Administrator", "01700000002", "Client Administrator", hash);
        seed("client.user@example.com", "CLIENT_USER", "Client User", "01700000003", "Support User", hash);
    }

    private void seed(String email, String role, String name, String mobile, String designation, String hash) {
        jdbc.update("INSERT INTO users (email,password_hash,role,name,mobile,designation,office) VALUES (?,?,?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE role=VALUES(role),name=VALUES(name),mobile=VALUES(mobile),designation=VALUES(designation),active=TRUE",
                email, hash, role, name, mobile, designation, "Dhaka");
    }
}
