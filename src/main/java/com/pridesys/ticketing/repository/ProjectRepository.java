package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByShortCode(String shortCode);
    @Query("select p from ProjectEntity p join ProjectMembershipEntity m on m.projectId=p.id where m.userId=:user")
    org.springframework.data.domain.Page<ProjectEntity> forUser(@org.springframework.data.repository.query.Param("user") long user, org.springframework.data.domain.Pageable pageable);

    @Query("select count(m)>0 from ProjectMembershipEntity m where m.projectId=:project and m.userId=:user")
    boolean member(@org.springframework.data.repository.query.Param("project") long p, @org.springframework.data.repository.query.Param("user") long u);
}
