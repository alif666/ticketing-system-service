package com.pridesys.ticketing.repository;
import com.pridesys.ticketing.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueCommentRepository extends JpaRepository<IssueCommentEntity,Long> { org.springframework.data.domain.Page<IssueCommentEntity> findByIssueIdOrderByCreatedAtAsc(long issueId,org.springframework.data.domain.Pageable pageable); }
