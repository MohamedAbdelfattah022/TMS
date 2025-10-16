package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.PagedResponse;
import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Category;
import com.mohamed.abdelfattah.tms.entities.Priority;
import com.mohamed.abdelfattah.tms.entities.Status;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.entities.User;
import com.mohamed.abdelfattah.tms.exceptions.ResourceNotFoundException;
import com.mohamed.abdelfattah.tms.mappers.TaskMapper;
import com.mohamed.abdelfattah.tms.repositories.ProjectRepository;
import com.mohamed.abdelfattah.tms.repositories.TaskRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        return TaskMapper.mapToDto(task);
    }

    public Integer createTask(Integer projectId, TaskDto taskDto) throws MessagingException {
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        var user = userRepository.findById(taskDto.getAssigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", taskDto.getAssigneeId()));

        Task task = TaskMapper.mapToEntity(taskDto, project, user);

        var data = emailService.createData(user, task);
        emailService.sendEmail(data);

        return taskRepository.save(task).getId();
    }

    public void updateTask(Integer id, TaskDto taskDto) throws MessagingException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        Integer oldAssigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;
        Integer newAssigneeId = taskDto.getAssigneeId();
        
        if (newAssigneeId != null && !newAssigneeId.equals(oldAssigneeId)) {
            User newAssignee = userRepository.findById(newAssigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", newAssigneeId));
            
            TaskMapper.mapToEntity(task, taskDto, newAssignee);
            
            var data = emailService.createTaskUpdateData(newAssignee, task);
            emailService.sendTaskUpdateEmail(data);
        } else {
            TaskMapper.mapToEntity(task, taskDto);
        }

        taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        taskRepository.deleteById(id);
    }

    public PagedResponse<TaskDto> searchTasks(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Task> taskPage = taskRepository.searchByTitleOrDescription(keyword, pageable);
        
        return buildPagedResponse(taskPage);
    }

    public PagedResponse<TaskDto> filterTasks(
            String title,
            String description,
            Status status,
            Priority priority,
            Category category,
            Integer assigneeId,
            Integer projectId,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Task> taskPage = taskRepository.searchWithFilters(
                title, description, status, priority, category, assigneeId, projectId, pageable
        );
        
        return buildPagedResponse(taskPage);
    }

    public PagedResponse<TaskDto> getTasksByProject(Integer projectId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Task> taskPage = taskRepository.findByProjectId(projectId, pageable);
        
        return buildPagedResponse(taskPage);
    }

    public PagedResponse<TaskDto> getTasksByAssignee(Integer assigneeId, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Task> taskPage = taskRepository.findByAssigneeId(assigneeId, pageable);
        
        return buildPagedResponse(taskPage);
    }

    public PagedResponse<TaskDto> getTasksByStatus(Status status, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Task> taskPage = taskRepository.findByStatus(status, pageable);
        
        return buildPagedResponse(taskPage);
    }

    private PagedResponse<TaskDto> buildPagedResponse(Page<Task> taskPage) {
        List<TaskDto> taskDtos = taskPage.getContent().stream()
                .map(TaskMapper::mapToDto)
                .toList();
        
        return PagedResponse.<TaskDto>builder()
                .content(taskDtos)
                .pageNumber(taskPage.getNumber())
                .pageSize(taskPage.getSize())
                .totalElements(taskPage.getTotalElements())
                .totalPages(taskPage.getTotalPages())
                .last(taskPage.isLast())
                .first(taskPage.isFirst())
                .build();
    }
}
