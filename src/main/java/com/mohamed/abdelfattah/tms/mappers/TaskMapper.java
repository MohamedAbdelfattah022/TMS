package com.mohamed.abdelfattah.tms.mappers;

import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Project;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;

import java.util.Optional;

public class TaskMapper {
    public static TaskDto mapToDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .category(task.getCategory())
                .attachmentUrl(task.getAttachmentUrl())
                .assigneeId(Optional.ofNullable(task.getAssignee()).map(User::getId).orElse(null))
                .projectId(Optional.ofNullable(task.getProject()).map(Project::getId).orElse(null))
                .build();
    }

    public static Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setCategory(taskDto.getCategory());
        task.setAttachmentUrl(taskDto.getAttachmentUrl());
        if (taskDto.getAssigneeId() != null) {
            User user = new User();
            user.setId(taskDto.getAssigneeId());
            task.setAssignee(user);
        }

        return task;
    }

    public static Task mapToEntity(TaskDto taskDto, Project project, User assignee) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setCategory(taskDto.getCategory());
        task.setAttachmentUrl(taskDto.getAttachmentUrl());
        task.setProject(project);
        task.setAssignee(assignee);

        return task;
    }

    public static void mapToEntity(Task task, TaskDto taskDto) {
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setCategory(taskDto.getCategory());
        task.setAttachmentUrl(taskDto.getAttachmentUrl());

        if (taskDto.getAssigneeId() == null) {
            task.setAssignee(null);
            return;
        }

        User user = new User();
        user.setId(taskDto.getAssigneeId());
        task.setAssignee(user);
    }

    public static void mapToEntity(Task task, TaskDto taskDto, User assignee) {
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setCategory(taskDto.getCategory());
        task.setAttachmentUrl(taskDto.getAttachmentUrl());
        task.setAssignee(assignee);
    }
}
