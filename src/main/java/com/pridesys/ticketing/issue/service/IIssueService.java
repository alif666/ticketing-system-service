package com.pridesys.ticketing.issue.service;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;

import java.util.*;

public interface IIssueService {
    PageResponse<IssueResponseDto> list(UserEntity actor, long projectId, String q, IssueStage stage, IssueType type, IssuePriority priority, Long moduleId, int page, int size);

    IssueResponseDto get(UserEntity actor, long id);

    IssueResponseDto create(UserEntity actor, CreateIssueRequest request);

    IssueResponseDto update(UserEntity actor, long id, UpdateIssueRequest request);

    IssueResponseDto move(UserEntity actor, long id, IssueStage stage);

    List<IssueAuditResponseDto> audit(UserEntity actor, long id);

    IssueResponseDto requestVerification(UserEntity actor, long id);

    PageResponse<IssueResponseDto> verificationQueue(UserEntity actor, int page, int size);

    IssueResponseDto approve(UserEntity actor, long id, VerificationDecisionRequest request);

    IssueResponseDto reject(UserEntity actor, long id, VerificationDecisionRequest request);
}
