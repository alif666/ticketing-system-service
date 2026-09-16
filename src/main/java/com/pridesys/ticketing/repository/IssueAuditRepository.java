package com.pridesys.ticketing.repository;

import com.pridesys.ticketing.entity.*;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueAuditRepository extends JpaRepository<IssueAuditEntity, Long> {
    List<IssueAuditEntity> findByIssueIdOrderByCreatedAtAsc(long issueId);
}
