package com.pridesys.ticketing.repository;
import com.pridesys.ticketing.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueCommentRepository extends JpaRepository<IssueCommentEntity,Long> { List<IssueCommentEntity> findByIssueIdOrderByCreatedAtAsc(long issueId); }
