package com.pridesys.ticketing.module.service;

import com.pridesys.ticketing.entity.ModuleEntity;
import com.pridesys.ticketing.entity.UserEntity;
import com.pridesys.ticketing.entity.UserRole;
import com.pridesys.ticketing.module.service.impl.ModuleServiceImpl;
import com.pridesys.ticketing.repository.ModuleRepository;
import com.pridesys.ticketing.project.service.IProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {
    @Mock
    ModuleRepository modules;
    @Mock
    IProjectService projects;
    @InjectMocks
    ModuleServiceImpl service;

    @Test
    void clientUserCanListModulesForAccessibleProject() {
        var actor = user(UserRole.CLIENT_USER, 7L);
        when(projects.hasAccess(actor, 12L)).thenReturn(true);
        var module = mock(ModuleEntity.class);
        when(module.getId()).thenReturn(3L);
        when(module.getProjectId()).thenReturn(12L);
        when(module.getName()).thenReturn("Portal");
        when(module.getDescription()).thenReturn("Support portal");
        when(module.isActive()).thenReturn(true);
        when(modules.findByProjectIdOrderByName(eq(12L), any())).thenReturn(new PageImpl<>(List.of(module)));

        var result = service.list(actor, 12L, 0, 20);

        assertEquals(1, result.totalElements());
        assertEquals("Portal", result.content().getFirst().name());
    }

    @Test
    void clientUserCannotCreateModules() {
        var actor = user(UserRole.CLIENT_USER, 7L);
        assertEquals(UserRole.CLIENT_USER, actor.getRole());
        when(projects.hasAccess(actor, 12L)).thenReturn(true);

        assertThrows(AccessDeniedException.class, () -> service.create(actor, 12L, new com.pridesys.ticketing.dto.CreateModuleRequest("Portal", "Support portal")));
        verifyNoInteractions(modules);
    }

    private UserEntity user(UserRole role, long id) {
        var user = new UserEntity("user@test.com", "hash", role, "User", 1L);
        user.setId(id);
        return user;
    }
}
