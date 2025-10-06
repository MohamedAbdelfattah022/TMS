package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.mappers.TaskMapper;
import com.mohamed.abdelfattah.tms.repositories.ProjectRepository;
import com.mohamed.abdelfattah.tms.repositories.TaskRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasksService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::mapToDto)
                .toList();
    }

    public TaskDto getTaskById(Integer id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return TaskMapper.mapToDto(task);
    }

    public Integer createTask(Integer projectId, TaskDto taskDto) throws MessagingException {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        Task task = TaskMapper.mapToEntity(taskDto);
        task.setProject(project);

        var user = userRepository.findById(taskDto.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + taskDto.getAssigneeId()));

        var data = emailService.createData(user, task);

        emailService.sendEmail(data);

        return taskRepository.save(task).getId();
    }

    public void updateTask(Integer id, TaskDto taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        TaskMapper.mapToEntity(task, taskDto);

        taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}
