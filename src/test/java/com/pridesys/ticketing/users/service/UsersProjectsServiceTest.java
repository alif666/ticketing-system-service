package com.pridesys.ticketing.users.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.pridesys.ticketing.entity.UserRecord;
import com.pridesys.ticketing.entity.UserRole;
import com.pridesys.ticketing.repository.UserRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class UsersProjectsServiceTest {
    @Mock UserRecordRepository users;

    @Test
    void clientAdminCannotCreateAppAdmin() {
        var service = new UserManagementService(users);
        var actor = new UserRecord(2, "admin@client.test", "hash", UserRole.CLIENT_ADMIN, "Admin", null, null, null, true);
        assertThrows(AccessDeniedException.class, () -> service.create(actor, new UserManagementService.CreateUser("x@test.com", "X", UserRole.APP_ADMIN, 1L)));
        verifyNoInteractions(users);
    }

    @Test
    void clientAdminCannotManageDifferentClientUser() {
        var service = new UserManagementService(users);
        var actor = new UserRecord(2, "admin@client.test", "hash", UserRole.CLIENT_ADMIN, "Admin", null, null, null, true);
        when(users.findById(9L)).thenReturn(java.util.Optional.of(new UserRecord(9, "other@test.com", "hash", UserRole.CLIENT_USER, "Other", null, null, null, true)));
        when(users.clientId(2L)).thenReturn(java.util.Optional.of(1L));
        when(users.clientId(9L)).thenReturn(java.util.Optional.of(2L));
        assertThrows(AccessDeniedException.class, () -> service.deactivate(actor, 9L));
    }
}
