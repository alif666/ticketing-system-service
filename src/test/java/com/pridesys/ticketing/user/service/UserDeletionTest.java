package com.pridesys.ticketing.user.service;

import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.exception.ResourceConflictException;
import com.pridesys.ticketing.repository.*;
import com.pridesys.ticketing.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDeletionTest {
    @Mock UserRepository users;
    @Mock PasswordEncoderStub encoder;
    @Mock ProjectMembershipRepository memberships;
    @Mock ResetTokenRepository resetTokens;
    @Mock IssueRepository issues;

    @Test
    void appAdminCanDeleteUserWithoutIssuesAndCleansReferences() {
        var target = user(UserRole.CLIENT_USER, 9L);
        when(users.findById(9L)).thenReturn(Optional.of(target));
        when(issues.existsByReporterId(9L)).thenReturn(false);
        var service = service();

        service.delete(user(UserRole.APP_ADMIN, 1L), 9L);

        verify(memberships).deleteByUserId(9L);
        verify(resetTokens).deleteByUserId(9L);
        verify(users).delete(target);
    }

    @Test
    void userWithIssuesCannotBeHardDeleted() {
        when(users.findById(9L)).thenReturn(Optional.of(user(UserRole.CLIENT_USER, 9L)));
        when(issues.existsByReporterId(9L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service().delete(user(UserRole.APP_ADMIN, 1L), 9L));
        verify(users, never()).deleteById(any(Long.class));
        verify(users, never()).delete(any(UserEntity.class));
    }

    @Test
    void clientAdminCannotDeleteUsers() {
        assertThrows(AccessDeniedException.class, () -> service().delete(user(UserRole.CLIENT_ADMIN, 2L), 9L));
        verifyNoInteractions(users, issues, memberships, resetTokens);
    }

    private UserServiceImpl service() {
        return new UserServiceImpl(users, encoder, memberships, resetTokens, issues);
    }

    private UserEntity user(UserRole role, long id) {
        var user = new UserEntity("user" + id + "@test.com", "hash", role, "User", 1L);
        user.setId(id);
        return user;
    }

    private interface PasswordEncoderStub extends org.springframework.security.crypto.password.PasswordEncoder {}
}
