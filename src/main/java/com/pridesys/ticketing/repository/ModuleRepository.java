package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
    org.springframework.data.domain.Page<ModuleEntity> findByProjectIdOrderByName(long projectId, org.springframework.data.domain.Pageable pageable);
}
