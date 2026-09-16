package com.pridesys.ticketing.repository;
import com.pridesys.ticketing.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueAttachmentRepository extends JpaRepository<IssueAttachmentEntity,Long> { List<IssueAttachmentEntity> findByIssueIdOrderByCreatedAtAsc(long issueId); }
