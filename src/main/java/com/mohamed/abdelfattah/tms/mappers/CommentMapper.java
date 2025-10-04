package com.mohamed.abdelfattah.tms.mappers;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.entities.Comment;

public final class CommentMapper {

    public static CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId().toString())
                .fullName(comment.getUser().getFullName())
                .build();
    }
}