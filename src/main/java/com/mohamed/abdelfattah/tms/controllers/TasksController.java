package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.PagedResponse;
import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Category;
import com.mohamed.abdelfattah.tms.entities.Priority;
import com.mohamed.abdelfattah.tms.entities.Status;
import com.mohamed.abdelfattah.tms.services.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "APIs for managing tasks - CRUD operations, search, filter, and sort")
public class TasksController {
    private final TasksService tasksService;

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task in the specified project and sends an email notification to the assignee"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Project or assignee not found")
    })
    @PostMapping("{project-id}")
    public ResponseEntity<Integer> createTask(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable("project-id") Integer projectId,
            @Valid @RequestBody TaskDto taskDto
    ) throws MessagingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(tasksService.createTask(projectId, taskDto));
    }

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks without pagination")
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        return ResponseEntity.ok(tasksService.getTasks());
    }

    @Operation(summary = "Get task by ID", description = "Retrieves a specific task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(
            @Parameter(description = "ID of the task", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(tasksService.getTaskById(id));
    }

    @Operation(
            summary = "Update a task",
            description = "Updates an existing task. Sends email notification if assignee is changed"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(
            @Parameter(description = "ID of the task to update", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody TaskDto taskDto
    ) throws MessagingException {
        tasksService.updateTask(id, taskDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true, example = "1")
            @PathVariable Integer id) {
        tasksService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search tasks",
            description = "Search tasks by title or description with pagination and sorting"
    )
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<TaskDto>> searchTasks(
            @Parameter(description = "Search keyword for title or description", required = true, example = "authentication")
            @RequestParam String keyword,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PagedResponse<TaskDto> response = tasksService.searchTasks(keyword, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Filter tasks",
            description = "Filter tasks by multiple criteria with pagination and sorting. " +
                    "All filter parameters are optional and can be combined"
    )
    @ApiResponse(responseCode = "200", description = "Filtered results retrieved successfully")
    @GetMapping("/filter")
    public ResponseEntity<PagedResponse<TaskDto>> filterTasks(
            @Parameter(description = "Filter by title (partial match)", example = "authentication")
            @RequestParam(required = false) String title,
            @Parameter(description = "Filter by description (partial match)", example = "JWT")
            @RequestParam(required = false) String description,
            @Parameter(description = "Filter by status", example = "TODO", schema = @Schema(implementation = Status.class))
            @RequestParam(required = false) Status status,
            @Parameter(description = "Filter by priority", example = "HIGH", schema = @Schema(implementation = Priority.class))
            @RequestParam(required = false) Priority priority,
            @Parameter(description = "Filter by category", example = "FEATURE", schema = @Schema(implementation = Category.class))
            @RequestParam(required = false) Category category,
            @Parameter(description = "Filter by assignee ID", example = "1")
            @RequestParam(required = false) Integer assigneeId,
            @Parameter(description = "Filter by project ID", example = "1")
            @RequestParam(required = false) Integer projectId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "priority")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PagedResponse<TaskDto> response = tasksService.filterTasks(
                title, description, status, priority, category, assigneeId, projectId,
                page, size, sortBy, sortDir
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get tasks by project",
            description = "Retrieves all tasks for a specific project with pagination"
    )
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<PagedResponse<TaskDto>> getTasksByProject(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable Integer projectId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PagedResponse<TaskDto> response = tasksService.getTasksByProject(projectId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get tasks by assignee",
            description = "Retrieves all tasks assigned to a specific user with pagination"
    )
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @GetMapping("/assignee/{assigneeId}")
    public ResponseEntity<PagedResponse<TaskDto>> getTasksByAssignee(
            @Parameter(description = "ID of the assignee", required = true, example = "1")
            @PathVariable Integer assigneeId,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PagedResponse<TaskDto> response = tasksService.getTasksByAssignee(assigneeId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get tasks by status",
            description = "Retrieves all tasks with a specific status with pagination"
    )
    @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<TaskDto>> getTasksByStatus(
            @Parameter(description = "Task status", required = true, example = "TODO", schema = @Schema(implementation = Status.class))
            @PathVariable Status status,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        PagedResponse<TaskDto> response = tasksService.getTasksByStatus(status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }
}
