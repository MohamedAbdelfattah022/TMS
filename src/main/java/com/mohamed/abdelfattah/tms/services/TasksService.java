package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Task;
import com.mohamed.abdelfattah.tms.mappers.TaskMapper;
import com.mohamed.abdelfattah.tms.repositories.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasksService {
    private final TaskRepository taskRepository;

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

    public Integer createTask(TaskDto taskDto) {
        Task task = TaskMapper.mapToEntity(taskDto);
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
