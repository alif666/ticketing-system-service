package com.pridesys.ticketing.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final ResetTokenRepository tokens; private final PasswordEncoder encoder; private final JwtService jwt;
    private final SecureRandom random = new SecureRandom();

    public AuthService(UserRepository users, ResetTokenRepository tokens, PasswordEncoder encoder, JwtService jwt) { this.users=users; this.tokens=tokens; this.encoder=encoder; this.jwt=jwt; }

    public AuthDtos.LoginResponse login(String email, String password) {
        UserRecord user = users.findByEmail(email).filter(UserRecord::active).orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        if (!encoder.matches(password, user.passwordHash())) throw new BadCredentialsException("Invalid email or password");
        return new AuthDtos.LoginResponse(jwt.issue(user), profile(user));
    }

    @Transactional
    public String requestReset(String email) {
        users.findByEmail(email).filter(UserRecord::active).ifPresent(user -> {
            Instant now = Instant.now(); tokens.invalidateOutstanding(user.id(), now);
            String raw = randomToken(); tokens.save(user.id(), hash(raw), now.plus(Duration.ofMinutes(15)));
            System.out.println("[DEV PASSWORD RESET] email=" + user.email() + " token=" + raw);
        });
        return "If an active account exists, a password reset token has been issued.";
    }

    @Transactional public void resetPassword(String raw, String newPassword) {
        var token = tokens.findUsable(hash(raw), Instant.now()).orElseThrow(() -> new BadCredentialsException("Invalid or expired reset token"));
        users.updatePassword(token.userId(), encoder.encode(newPassword)); tokens.markUsed(token.id(), Instant.now());
    }

    @Transactional public void changePassword(long id, String current, String next) {
        UserRecord user = users.findById(id).orElseThrow();
        if (!encoder.matches(current, user.passwordHash())) throw new BadCredentialsException("Current password is incorrect");
        users.updatePassword(id, encoder.encode(next));
    }

    public AuthDtos.ProfileResponse profile(long id) { return profile(users.findById(id).orElseThrow()); }
    @Transactional public AuthDtos.ProfileResponse updateProfile(long id, AuthDtos.UpdateProfileRequest req) { users.updateProfile(id, req.name().trim(), req.mobile(), req.designation(), req.office()); return profile(id); }
    public AuthDtos.ProfileResponse profile(UserRecord u) { return new AuthDtos.ProfileResponse(u.id(),u.email(),u.role().name(),u.name(),u.mobile(),u.designation(),u.office(),u.active()); }
    private String randomToken() { byte[] bytes = new byte[32]; random.nextBytes(bytes); return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); }
    private String hash(String value) { try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); } catch (Exception e) { throw new IllegalStateException(e); } }
}
