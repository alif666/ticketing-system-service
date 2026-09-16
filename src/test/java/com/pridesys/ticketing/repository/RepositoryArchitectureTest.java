package com.pridesys.ticketing.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pridesys.ticketing.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

class RepositoryArchitectureTest {
    @Test
    void domainRepositoriesUseSpringDataJpa() {
        assertTrue(JpaRepository.class.isAssignableFrom(UserRepository.class));
        assertTrue(JpaRepository.class.isAssignableFrom(ClientRepository.class));
        assertTrue(JpaRepository.class.isAssignableFrom(ProjectRepository.class));
        assertTrue(JpaRepository.class.isAssignableFrom(ModuleRepository.class));
        assertTrue(JpaRepository.class.isAssignableFrom(ProjectMembershipRepository.class));
    }
}
