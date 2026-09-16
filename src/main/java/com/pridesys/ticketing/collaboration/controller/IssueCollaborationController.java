package com.pridesys.ticketing.collaboration.controller;

import com.pridesys.ticketing.dto.*;
import com.pridesys.ticketing.entity.*;
import com.pridesys.ticketing.comment.service.CommentService;
import com.pridesys.ticketing.attachment.service.AttachmentService;
import com.pridesys.ticketing.repository.UserRepository;
import com.pridesys.ticketing.security.util.JwtService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api/issues")
public class IssueCollaborationController {
    private final CommentService comments;
    private final AttachmentService attachments;
    private final UserRepository users;

    public IssueCollaborationController(CommentService c, AttachmentService a, UserRepository u) {
        comments = c;
        attachments = a;
        users = u;
    }

    private UserEntity actor(Authentication a) {
        return users.findById(((JwtService.UserPrincipal) a.getPrincipal()).id()).orElseThrow();
    }

    @GetMapping("/{issueId}/comments")
    public PageResponse<CommentResponseDto> comments(Authentication a, @PathVariable long issueId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return comments.list(actor(a), issueId, page, size);
    }

    @PostMapping("/{issueId}/comments")
    public CommentResponseDto createComment(Authentication a, @PathVariable long issueId, @Valid @RequestBody CommentRequest r) {
        return comments.create(actor(a), issueId, r);
    }

    @PatchMapping("/comments/{id}")
    public CommentResponseDto updateComment(Authentication a, @PathVariable long id, @Valid @RequestBody CommentRequest r) {
        return comments.update(actor(a), id, r);
    }

    @DeleteMapping("/comments/{id}")
    public Map<String, String> deleteComment(Authentication a, @PathVariable long id) {
        comments.delete(actor(a), id);
        return Map.of("message", "Comment deleted");
    }

    @GetMapping("/{issueId}/attachments")
    public PageResponse<AttachmentResponseDto> attachments(Authentication a, @PathVariable long issueId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return attachments.list(actor(a), issueId, page, size);
    }

    @PostMapping(value = "/{issueId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttachmentResponseDto upload(Authentication a, @PathVariable long issueId, @RequestPart MultipartFile file) throws IOException {
        return attachments.upload(actor(a), issueId, file);
    }

    @GetMapping("/attachments/{id}/download")
    public ResponseEntity<InputStreamResource> download(Authentication a, @PathVariable long id) throws IOException {
        var x = attachments.download(actor(a), id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(new InputStreamResource(x));
    }

    @DeleteMapping("/attachments/{id}")
    public Map<String, String> deleteAttachment(Authentication a, @PathVariable long id) throws IOException {
        attachments.delete(actor(a), id);
        return Map.of("message", "Attachment deleted");
    }
}
