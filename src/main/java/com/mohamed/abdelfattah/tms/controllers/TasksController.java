package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.services.TasksService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TasksController {
    private final TasksService tasksService;

    @PostMapping("{project-id}")
    public ResponseEntity<Integer> createTask(
            @PathVariable("project-id") Integer projectId,
            @RequestBody TaskDto taskDto
    ) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(tasksService.createTask(projectId, taskDto));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(tasksService.getTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer id) {
        return ResponseEntity.ok(tasksService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        tasksService.updateTask(id, taskDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        tasksService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
