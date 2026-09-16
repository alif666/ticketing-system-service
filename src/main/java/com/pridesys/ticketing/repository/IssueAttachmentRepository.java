package com.pridesys.ticketing.repository;
import com.pridesys.ticketing.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueAttachmentRepository extends JpaRepository<IssueAttachmentEntity,Long> { org.springframework.data.domain.Page<IssueAttachmentEntity> findByIssueIdOrderByCreatedAtAsc(long issueId,org.springframework.data.domain.Pageable pageable); }
