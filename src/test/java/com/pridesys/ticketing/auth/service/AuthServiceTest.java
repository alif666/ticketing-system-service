package com.pridesys.ticketing.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.pridesys.ticketing.entity.UserRecord;
import com.pridesys.ticketing.entity.UserRole;
import com.pridesys.ticketing.repository.ResetTokenRecordRepository;
import com.pridesys.ticketing.repository.UserRecordRepository;
import com.pridesys.ticketing.security.util.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRecordRepository users;
    @Mock ResetTokenRecordRepository tokens;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwt;
    @InjectMocks AuthService service;

    @Test void loginRejectsInactiveUser() {
        when(users.findByEmail("inactive@example.com")).thenReturn(Optional.of(user(false)));
        assertThatThrownBy(() -> service.login("inactive@example.com", "Password123!"))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(encoder, jwt);
    }

    @Test void loginRejectsWrongPassword() {
        when(users.findByEmail("user@example.com")).thenReturn(Optional.of(user(true)));
        when(encoder.matches("wrong", "stored-hash")).thenReturn(false);
        assertThatThrownBy(() -> service.login("user@example.com", "wrong"))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(jwt);
    }

    @Test void loginIssuesJwtForActiveUser() {
        UserRecord user = user(true);
        when(users.findByEmail(user.email())).thenReturn(Optional.of(user));
        when(encoder.matches("Password123!", user.passwordHash())).thenReturn(true);
        when(jwt.issue(user)).thenReturn("jwt");
        var response = service.login(user.email(), "Password123!");
        org.assertj.core.api.Assertions.assertThat(response.token()).isEqualTo("jwt");
        verify(jwt).issue(user);
    }

    @Test void resetTokenMustBeUsableAndIsMarkedUsed() {
        when(tokens.findUsable(anyString(), any(Instant.class))).thenReturn(Optional.of(new ResetTokenRecordRepository.TokenRecord(7, 2, Instant.now().plusSeconds(30), null)));
        when(encoder.encode("NewPassword123!")).thenReturn("new-hash");
        service.resetPassword("token", "NewPassword123!");
        verify(users).updatePassword(2, "new-hash");
        verify(tokens).markUsed(eq(7L), any(Instant.class));
    }

    @Test void expiredOrUsedResetTokenIsRejected() {
        when(tokens.findUsable(anyString(), any(Instant.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.resetPassword("token", "NewPassword123!"))
                .isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(encoder, users);
    }

    @Test void passwordChangeRequiresCurrentPassword() {
        UserRecord user = user(true);
        when(users.findById(1)).thenReturn(Optional.of(user));
        when(encoder.matches("wrong", user.passwordHash())).thenReturn(false);
        assertThatThrownBy(() -> service.changePassword(1, "wrong", "NewPassword123!"))
                .isInstanceOf(BadCredentialsException.class);
        verify(users, never()).updatePassword(anyLong(), anyString());
    }

    private UserRecord user(boolean active) { return new UserRecord(1, "user@example.com", "stored-hash", UserRole.CLIENT_USER, "Test User", "01700000000", "Tester", "Dhaka", active); }
}
