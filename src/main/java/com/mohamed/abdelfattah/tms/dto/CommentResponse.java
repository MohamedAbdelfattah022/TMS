package com.mohamed.abdelfattah.tms.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Integer id,
        String content,
        Integer taskId,
        Integer userId,
        String fullName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
