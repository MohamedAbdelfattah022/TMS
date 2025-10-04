package com.mohamed.abdelfattah.tms.dto;

import lombok.Builder;

@Builder
public record CommentResponse(
        Integer id,
        String content,
        String userId,
        String fullName
) {
}
