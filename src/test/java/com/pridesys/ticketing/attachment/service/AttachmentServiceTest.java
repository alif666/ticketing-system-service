package com.pridesys.ticketing.attachment.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {
    @Mock IssueAttachmentRepository attachments;
    @Mock IssueRepository issues;
    @Mock ProjectRepository projects;
    @Mock FileStorage storage;
    @InjectMocks AttachmentService service;

    @Test
    void rejectsUnsupportedFileType() {
        var actor = user(1L);
        when(issues.findById(1L)).thenReturn(Optional.of(issue(1L)));
        when(projects.member(1L, 1L)).thenReturn(true);
        var file = new MockMultipartFile("file", "script.exe", "application/x-msdownload", "x".getBytes());
        assertThrows(IllegalArgumentException.class, () -> service.upload(actor, 1L, file));
        verifyNoInteractions(storage);
    }

    @Test
    void downloadRequiresIssueAccess() throws Exception {
        var actor = user(1L);
        var attachment = new IssueAttachmentEntity(1L, 2L, "a.txt", "key", "text/plain", 1L);
        attachment.setId(3L);
        when(attachments.findById(3L)).thenReturn(Optional.of(attachment));
        when(issues.findById(1L)).thenReturn(Optional.of(issue(9L)));
        when(projects.member(9L, 1L)).thenReturn(false);
        assertThrows(AccessDeniedException.class, () -> service.download(actor, 3L));
        verifyNoInteractions(storage);
    }

    private IssueEntity issue(long projectId) {
        var issue = new IssueEntity("Issue", "Description", IssueType.BUG, IssuePriority.HIGH, projectId, null, 2L);
        issue.setId(1L);
        return issue;
    }

    private UserEntity user(long id) {
        var user = new UserEntity("user@test.com", "hash", UserRole.CLIENT_USER, "User", 1L);
        user.setId(id);
        return user;
    }
}
