package com.pridesys.ticketing.comment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;
import com.pridesys.ticketing.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    IssueCommentRepository comments;
    @Mock
    IssueRepository issues;
    @Mock
    ProjectRepository projects;
    @InjectMocks
    CommentService service;

    @Test
    void onlyAuthorMayEdit() {
        var c = new IssueCommentEntity(1, 4, "old");
        c.setId(2L);
        when(comments.findById(2L)).thenReturn(Optional.of(c));
        var u = new UserEntity();
        u.setId(5L);
        assertThrows(AccessDeniedException.class, () -> service.update(u, 2, new CommentRequest("new")));
    }
}
