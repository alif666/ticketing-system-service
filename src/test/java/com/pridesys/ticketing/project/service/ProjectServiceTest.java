package com.pridesys.ticketing.project.service;

import com.pridesys.ticketing.entity.UserEntity;
import com.pridesys.ticketing.entity.UserRole;
import com.pridesys.ticketing.project.service.impl.ProjectServiceImpl;
import com.pridesys.ticketing.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock ProjectRepository projects;
    @Mock ProjectMembershipRepository memberships;
    @Mock UserRepository users;
    @Mock IssueRepository issues;
    @Mock ModuleRepository modules;

    @Test
    void appAdminCanListProjectMembersWithoutCredentialData() {
        var member = user(UserRole.CLIENT_USER, 9L);
        when(users.findByProjectId(eq(12L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(member)));
        var service = new ProjectServiceImpl(projects, memberships, users, issues, modules);

        var result = service.members(user(UserRole.APP_ADMIN, 1L), 12L, 0, 20);

        assertEquals(1, result.totalElements());
        assertEquals("member@test.com", result.content().getFirst().email());
        assertEquals("CLIENT_USER", result.content().getFirst().role());
        verify(users).findByProjectId(eq(12L), any(Pageable.class));
    }

    @Test
    void clientAdminCannotListProjectMembers() {
        var service = new ProjectServiceImpl(projects, memberships, users, issues, modules);

        assertThrows(AccessDeniedException.class,
                () -> service.members(user(UserRole.CLIENT_ADMIN, 2L), 12L, 0, 20));
        verifyNoInteractions(users);
    }

    private UserEntity user(UserRole role, long id) {
        var user = new UserEntity(id == 9L ? "member@test.com" : "admin@test.com", "secret-hash", role, "Test User", 1L);
        user.setId(id);
        user.setMobile("01700000000");
        user.setDesignation("Support");
        user.setOffice("Dhaka");
        return user;
    }
}
