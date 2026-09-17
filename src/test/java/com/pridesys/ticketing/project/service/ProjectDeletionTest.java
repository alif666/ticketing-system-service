package com.pridesys.ticketing.project.service;

import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.exception.ResourceConflictException;
import com.pridesys.ticketing.project.service.impl.ProjectServiceImpl;
import com.pridesys.ticketing.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectDeletionTest {
    @Mock ProjectRepository projects;
    @Mock ProjectMembershipRepository memberships;
    @Mock UserRepository users;
    @Mock IssueRepository issues;
    @Mock ModuleRepository modules;

    @Test
    void appAdminDeletesProjectAndCascadesOwnedRowsWhenNoIssuesExist() {
        var project = new ProjectEntity("Demo", "DEMO", "Demo");
        project.setId(12L);
        when(projects.findById(12L)).thenReturn(Optional.of(project));
        when(issues.existsByProjectId(12L)).thenReturn(false);
        var service = new ProjectServiceImpl(projects, memberships, users, issues, modules);

        service.delete(user(UserRole.APP_ADMIN, 1L), 12L);

        verify(memberships).deleteByProjectId(12L);
        verify(modules).deleteByProjectId(12L);
        verify(projects).delete(project);
    }

    @Test
    void projectWithIssuesCannotBeDeleted() {
        when(projects.findById(12L)).thenReturn(Optional.of(new ProjectEntity("Demo", "DEMO", "Demo")));
        when(issues.existsByProjectId(12L)).thenReturn(true);

        assertThrows(ResourceConflictException.class, () -> service().delete(user(UserRole.APP_ADMIN, 1L), 12L));
        verifyNoInteractions(memberships, modules);
    }

    @Test
    void clientAdminCannotDeleteProjects() {
        assertThrows(AccessDeniedException.class, () -> service().delete(user(UserRole.CLIENT_ADMIN, 2L), 12L));
        verifyNoInteractions(projects, issues, memberships, modules);
    }

    private ProjectServiceImpl service() { return new ProjectServiceImpl(projects, memberships, users, issues, modules); }
    private UserEntity user(UserRole role, long id) {
        var user = new UserEntity("user@test.com", "hash", role, "User", 1L);
        user.setId(id);
        return user;
    }
}
