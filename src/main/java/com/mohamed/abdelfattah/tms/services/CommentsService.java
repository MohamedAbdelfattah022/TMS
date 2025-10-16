package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.dto.CreateCommentRequest;
import com.mohamed.abdelfattah.tms.dto.UpdateCommentRequest;
import com.mohamed.abdelfattah.tms.entities.Comment;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;
import com.mohamed.abdelfattah.tms.exceptions.ResourceNotFoundException;
import com.mohamed.abdelfattah.tms.mappers.CommentMapper;
import com.mohamed.abdelfattah.tms.repositories.CommentRepository;
import com.mohamed.abdelfattah.tms.repositories.TaskRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public CommentResponse findTaskComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        return CommentMapper.mapToResponse(comment);
    }

    public List<CommentResponse> findAllComments(Integer taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(CommentMapper::mapToResponse)
                .toList();
    }

    public Integer createComment(Integer taskId, CreateCommentRequest request) throws MessagingException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        User commenter = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Comment comment = CommentMapper.mapToEntity(request, task, commenter);
        Comment savedComment = commentRepository.save(comment);

        if (task.getAssignee() != null && !task.getAssignee().getId().equals(commenter.getId()))
            emailService.sendCommentNotificationEmail(task.getAssignee(), task, commenter, savedComment);

        return savedComment.getId();
    }

    public Integer updateComment(Integer commentId, UpdateCommentRequest request) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        CommentMapper.mapToEntity(existingComment, request);
        return commentRepository.save(existingComment).getId();
    }

    public void deleteComment(Integer commentId) {
        if (!commentRepository.existsById(commentId))
            throw new ResourceNotFoundException("Comment", "id", commentId);

        commentRepository.deleteById(commentId);
    }
}
