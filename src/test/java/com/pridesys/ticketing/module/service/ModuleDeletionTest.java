package com.pridesys.ticketing.module.service;

import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.exception.ResourceConflictException;
import com.pridesys.ticketing.module.service.impl.ModuleServiceImpl;
import com.pridesys.ticketing.project.service.IProjectService;
import com.pridesys.ticketing.repository.IssueRepository;
import com.pridesys.ticketing.repository.ModuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleDeletionTest {
    @Mock ModuleRepository modules;
    @Mock IProjectService projects;
    @Mock IssueRepository issues;

    @Test
    void moduleWithIssuesCannotBeDeleted() {
        when(projects.hasAccess(any(), eq(12L))).thenReturn(true);
        var module = new ModuleEntity(12L, "Portal", "Portal");
        module.setId(3L);
        when(modules.findById(3L)).thenReturn(java.util.Optional.of(module));
        when(issues.existsByModuleId(3L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service().delete(user(UserRole.APP_ADMIN), 12L, 3L));
        verify(modules, never()).deleteById(3L);
    }

    @Test
    void accessibleModuleCanBeDeletedWhenItHasNoIssues() {
        when(projects.hasAccess(any(), eq(12L))).thenReturn(true);
        var module = new ModuleEntity(12L, "Portal", "Portal");
        module.setId(3L);
        when(modules.findById(3L)).thenReturn(java.util.Optional.of(module));
        when(issues.existsByModuleId(3L)).thenReturn(false);

        service().delete(user(UserRole.CLIENT_ADMIN), 12L, 3L);

        verify(modules).deleteById(3L);
    }

    @Test
    void clientUserCannotDeleteModules() {
        when(projects.hasAccess(any(), eq(12L))).thenReturn(true);
        assertThrows(AccessDeniedException.class, () -> service().delete(user(UserRole.CLIENT_USER), 12L, 3L));
        verifyNoInteractions(issues, modules);
    }

    private ModuleServiceImpl service() { return new ModuleServiceImpl(modules, projects, issues); }
    private UserEntity user(UserRole role) { return new UserEntity("user@test.com", "hash", role, "User", 1L); }
}
