package com.pridesys.ticketing.dto;
import java.time.Instant;
public record CommentResponseDto(long id,long issueId,long authorId,String body,boolean edited,Instant createdAt,Instant updatedAt) {}
