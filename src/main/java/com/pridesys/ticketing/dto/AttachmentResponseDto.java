package com.pridesys.ticketing.dto;
import java.time.Instant;
public record AttachmentResponseDto(long id,long issueId,long uploaderId,String originalName,String contentType,long sizeBytes,Instant createdAt) {}
