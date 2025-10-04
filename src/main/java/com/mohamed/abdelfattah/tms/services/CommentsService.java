package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.CommentResponse;
import com.mohamed.abdelfattah.tms.dto.CreateCommentRequest;
import com.mohamed.abdelfattah.tms.dto.UpdateCommentRequest;
import com.mohamed.abdelfattah.tms.entities.Comment;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;
import com.mohamed.abdelfattah.tms.mappers.CommentMapper;
import com.mohamed.abdelfattah.tms.repositories.CommentRepository;
import com.mohamed.abdelfattah.tms.repositories.TaskRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentResponse findTaskComment(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        return CommentMapper.mapToResponse(comment);
    }

    public List<CommentResponse> findAllComments(Integer taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(CommentMapper::mapToResponse)
                .toList();
    }

    public CommentResponse createComment(Integer taskId, CreateCommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.mapToResponse(savedComment);
    }

    public CommentResponse updateComment(Integer commentId, UpdateCommentRequest request) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        existingComment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(existingComment);
        return CommentMapper.mapToResponse(updatedComment);
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}
