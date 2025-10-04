package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.dto.CreateCommentRequest;
import com.mohamed.abdelfattah.tms.dto.UpdateCommentRequest;
import com.mohamed.abdelfattah.tms.services.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService commentsService;

    @PostMapping("/tasks/{task-id}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable("task-id") Integer taskId,
            @RequestBody CreateCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentsService.createComment(taskId, request));
    }

    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable("comment-id") Integer commentId,
            @RequestBody UpdateCommentRequest request
    ) {
        return ResponseEntity.ok(commentsService.updateComment(commentId, request));
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("comment-id") Integer commentId
    ) {
        commentsService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comment/{comment-id}")
    public ResponseEntity<CommentResponse> findTaskComment(
            @PathVariable("comment-id") Integer commentId
    ) {
        return ResponseEntity.ok(commentsService.findTaskComment(commentId));
    }

    @GetMapping("/tasks/{task-id}/comments")
    public ResponseEntity<List<CommentResponse>> findAllComments(
            @PathVariable("task-id") Integer taskId
    ) {
        return ResponseEntity.ok(commentsService.findAllComments(taskId));
    }
}
