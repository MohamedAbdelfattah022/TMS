package com.mohamed.abdelfattah.tms.mappers;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.dto.CreateCommentRequest;
import com.mohamed.abdelfattah.tms.dto.UpdateCommentRequest;
import com.mohamed.abdelfattah.tms.entities.Comment;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;

public final class CommentMapper {

    public static CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .fullName(comment.getUser().getFullName())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static Comment mapToEntity(CreateCommentRequest request, Task task, User user) {
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);
        return comment;
    }

    public static void mapToEntity(Comment comment, UpdateCommentRequest request) {
        comment.setContent(request.getContent());
    }
}