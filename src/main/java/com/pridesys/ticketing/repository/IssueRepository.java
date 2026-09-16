package com.pridesys.ticketing.repository;
import com.pridesys.ticketing.entity.*; import org.springframework.data.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueRepository extends JpaRepository<IssueEntity,Long> { Page<IssueEntity> findByProjectId(long projectId,Pageable pageable); Page<IssueEntity> findByProjectIdAndTitleContainingIgnoreCase(long projectId,String title,Pageable pageable); }
