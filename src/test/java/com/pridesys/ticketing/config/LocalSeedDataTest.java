package com.pridesys.ticketing.config;

import com.pridesys.ticketing.entity.ProjectEntity;
import com.pridesys.ticketing.entity.ProjectMembershipId;
import com.pridesys.ticketing.entity.UserEntity;
import com.pridesys.ticketing.entity.UserRole;
import com.pridesys.ticketing.repository.ClientRepository;
import com.pridesys.ticketing.repository.ModuleRepository;
import com.pridesys.ticketing.repository.ProjectMembershipRepository;
import com.pridesys.ticketing.repository.ProjectRepository;
import com.pridesys.ticketing.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocalSeedDataTest {
    @Mock UserRepository users;
    @Mock ClientRepository clients;
    @Mock ProjectRepository projects;
    @Mock ModuleRepository modules;
    @Mock ProjectMembershipRepository memberships;
    @Mock PasswordEncoder encoder;

    @Test
    void seedsDemoProjectAndMembershipsOnlyWhenTheyAreMissing() {
        var app = user("app.admin@example.com", UserRole.APP_ADMIN, null, 1L);
        var clientAdmin = user("client.admin@example.com", UserRole.CLIENT_ADMIN, 1L, 2L);
        var clientUser = user("client.user@example.com", UserRole.CLIENT_USER, 1L, 3L);
        var project = new ProjectEntity("Customer Support Portal", "DEMO", "Seeded demo workspace");
        project.setId(100L);
        when(clients.findByName("Acme Corporation")).thenReturn(Optional.empty());
        when(clients.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.findByEmailIgnoreCase("app.admin@example.com")).thenReturn(Optional.of(app));
        when(users.findByEmailIgnoreCase("client.admin@example.com")).thenReturn(Optional.of(clientAdmin));
        when(users.findByEmailIgnoreCase("client.user@example.com")).thenReturn(Optional.of(clientUser));
        when(users.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(encoder.encode("Password123!")).thenReturn("encoded");
        when(projects.findByShortCode("DEMO")).thenReturn(Optional.of(project));
        when(modules.findByProjectIdAndName(any(Long.class), any())).thenReturn(Optional.empty());
        when(modules.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(memberships.existsById(any(ProjectMembershipId.class))).thenReturn(false);

        new LocalSeedData(users, clients, projects, modules, memberships, encoder, true).run();

        verify(projects, never()).save(any());
        verify(modules, org.mockito.Mockito.times(2)).save(any());
        verify(memberships, org.mockito.Mockito.times(2)).save(any());
    }

    private UserEntity user(String email, UserRole role, Long clientId, long id) {
        var user = new UserEntity(email, "hash", role, role.name(), clientId);
        user.setId(id);
        return user;
    }
}
