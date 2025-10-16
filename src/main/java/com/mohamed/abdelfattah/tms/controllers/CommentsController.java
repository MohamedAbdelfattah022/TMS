package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.dto.CreateCommentRequest;
import com.mohamed.abdelfattah.tms.dto.UpdateCommentRequest;
import com.mohamed.abdelfattah.tms.services.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comment Management", description = "APIs for managing comments on tasks")
public class CommentsController {
    private final CommentsService commentsService;

    @Operation(
            summary = "Create a comment",
            description = "Creates a new comment on a task and sends email notification to the task assignee"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Task or user not found")
    })
    @PostMapping("/tasks/{task-id}/comments")
    public ResponseEntity<Integer> createComment(
            @Parameter(description = "ID of the task", required = true, example = "1")
            @PathVariable("task-id") Integer taskId,
            @Valid @RequestBody CreateCommentRequest request
    ) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentsService.createComment(taskId, request));
    }

    @Operation(summary = "Update a comment", description = "Updates an existing comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<Integer> updateComment(
            @Parameter(description = "ID of the comment to update", required = true, example = "1")
            @PathVariable("comment-id") Integer commentId,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commentsService.updateComment(commentId, request));
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "ID of the comment to delete", required = true, example = "1")
            @PathVariable("comment-id") Integer commentId
    ) {
        commentsService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get comment by ID", description = "Retrieves a specific comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("/comment/{comment-id}")
    public ResponseEntity<CommentResponse> findTaskComment(
            @Parameter(description = "ID of the comment", required = true, example = "1")
            @PathVariable("comment-id") Integer commentId
    ) {
        return ResponseEntity.ok(commentsService.findTaskComment(commentId));
    }

    @Operation(summary = "Get all comments for a task", description = "Retrieves all comments for a specific task")
    @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    @GetMapping("/tasks/{task-id}/comments")
    public ResponseEntity<List<CommentResponse>> findAllComments(
            @Parameter(description = "ID of the task", required = true, example = "1")
            @PathVariable("task-id") Integer taskId
    ) {
        return ResponseEntity.ok(commentsService.findAllComments(taskId));
    }
}
