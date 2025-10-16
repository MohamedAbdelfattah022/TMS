package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.*;
import com.mohamed.abdelfattah.tms.entities.Project;
import com.mohamed.abdelfattah.tms.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
@Tag(name = "Project Management", description = "APIs for managing projects, members, and roles")
public class ProjectsController {
    private final ProjectService projectService;

    @Operation(summary = "Create a new project", description = "Creates a new project with name and description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Integer> createProject(@Valid @RequestBody CreateProjectRequestDto request) {
        Project savedProject = projectService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject.getId());
    }

    @Operation(summary = "Get project by ID", description = "Retrieves project details including members and tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<ProjectDetailsDto> findProjectById(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable Integer id) {
        ProjectDetailsDto projectDetailsDto = projectService.findById(id);
        return ResponseEntity.ok().body(projectDetailsDto);
    }

    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects without pagination")
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ProjectDetailsDto>> findAllProjects() {
        var projects = projectService.findAll();
        return ResponseEntity.ok().body(projects);
    }

    @Operation(
            summary = "Get projects with pagination",
            description = "Retrieves all projects with pagination and sorting support"
    )
    @ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    @GetMapping("/paginated")
    public ResponseEntity<PagedResponse<ProjectDetailsDto>> findAllProjectsPaginated(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "projectName")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PagedResponse<ProjectDetailsDto> response = projectService.findAllPaginated(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search projects", description = "Search projects by name or description with pagination")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<ProjectDetailsDto>> searchProjects(
            @Parameter(description = "Search keyword for project name or description", required = true, example = "task management")
            @RequestParam String keyword,
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "projectName")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PagedResponse<ProjectDetailsDto> response = projectService.searchProjects(keyword, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a project", description = "Updates project name and/or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> updateProject(
            @Parameter(description = "ID of the project to update", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProjectRequestDto request
    ) {
        projectService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a project", description = "Deletes a project and all its associated data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete", required = true, example = "1")
            @PathVariable Integer id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Add member to project",
            description = "Adds a user to a project with a specific role (ADMIN, MANAGER, or DEVELOPER)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member added successfully"),
            @ApiResponse(responseCode = "400", description = "User is already a member or invalid data"),
            @ApiResponse(responseCode = "404", description = "Project or user not found")
    })
    @PostMapping("{projectId}/members")
    public ResponseEntity<Void> addMemberToProject(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable Integer projectId,
            @Valid @RequestBody AddMemberRequest request
    ) {
        projectService.addMemberToProject(projectId, request.getUserId(), request.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update member role", description = "Updates the role of an existing project member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project member not found")
    })
    @PutMapping("{projectId}/members/{userId}/role")
    public ResponseEntity<Void> updateMemberRole(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable Integer projectId,
            @Parameter(description = "ID of the user", required = true, example = "1")
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateMemberRoleRequest request
    ) {
        projectService.updateMemberRole(projectId, userId, request.getRole());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove member from project", description = "Removes a user from a project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Member removed successfully"),
            @ApiResponse(responseCode = "404", description = "Project member not found")
    })
    @DeleteMapping("{projectId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromProject(
            @Parameter(description = "ID of the project", required = true, example = "1")
            @PathVariable Integer projectId,
            @Parameter(description = "ID of the user to remove", required = true, example = "1")
            @PathVariable Integer userId
    ) {
        projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }
}
