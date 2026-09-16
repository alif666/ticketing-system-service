package com.pridesys.ticketing.repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ResetTokenRepository {
    private final JdbcTemplate jdbc;

    public ResetTokenRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void invalidateOutstanding(long userId, Instant now) {
        jdbc.update("UPDATE password_reset_tokens SET used_at=? WHERE user_id=? AND used_at IS NULL", Timestamp.from(now), userId);
    }

    public void save(long userId, String hash, Instant expiresAt) {
        jdbc.update("INSERT INTO password_reset_tokens (user_id,token_hash,expires_at) VALUES (?,?,?)", userId, hash, Timestamp.from(expiresAt));
    }

    public Optional<TokenRecord> findUsable(String hash, Instant now) {
        return jdbc.query("SELECT id,user_id,expires_at,used_at FROM password_reset_tokens WHERE token_hash=?",
                (rs, row) -> new TokenRecord(rs.getLong("id"), rs.getLong("user_id"), rs.getTimestamp("expires_at").toInstant(),
                        rs.getTimestamp("used_at") == null ? null : rs.getTimestamp("used_at").toInstant()), hash).stream()
                .filter(token -> token.usedAt() == null && token.expiresAt().isAfter(now)).findFirst();
    }

    public void markUsed(long id, Instant now) { jdbc.update("UPDATE password_reset_tokens SET used_at=? WHERE id=?", Timestamp.from(now), id); }

    public record TokenRecord(long id, long userId, Instant expiresAt, Instant usedAt) {}
}
