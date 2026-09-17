package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMembershipRepository extends JpaRepository<ProjectMembershipEntity, ProjectMembershipId> {
    void deleteByUserId(long userId);

    void deleteByProjectId(long projectId);
    @Query("select m.id.projectId from ProjectMembershipEntity m where m.id.userId = :userId")
    List<Long> findProjectIdsByUserId(@Param("userId") long userId);
}
