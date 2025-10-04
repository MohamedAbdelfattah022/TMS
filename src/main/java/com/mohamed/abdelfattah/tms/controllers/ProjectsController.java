package com.mohamed.abdelfattah.tms.controllers;

import com.mohamed.abdelfattah.tms.dto.CreateProjectRequestDto;
import com.mohamed.abdelfattah.tms.dto.ProjectDetailsDto;
import com.mohamed.abdelfattah.tms.dto.UpdateProjectRequestDto;
import com.mohamed.abdelfattah.tms.entities.Project;
import com.mohamed.abdelfattah.tms.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects")
@RequiredArgsConstructor
public class ProjectsController {
    private final ProjectService projectService;

    @PostMapping("{projectId}/members/{userId}")
    public ResponseEntity<Void> addMemberToProject(@PathVariable Integer projectId, @PathVariable Integer userId) {
        projectService.addMemberToProject(projectId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{projectId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromProject(@PathVariable Integer projectId, @PathVariable Integer userId) {
        projectService.removeMemberFromProject(projectId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectDetailsDto> findProjectById(@PathVariable Integer id) {
        ProjectDetailsDto projectDetailsDto = projectService.findById(id);
        return ResponseEntity.ok().body(projectDetailsDto);
    }

    @GetMapping()
    public ResponseEntity<List<ProjectDetailsDto>> findAllProjects() {
        var project = projectService.findAll();
        return ResponseEntity.ok().body(project);
    }

    @PostMapping
    public ResponseEntity<Integer> createProject(@RequestBody CreateProjectRequestDto request) {
        Project savedProject = projectService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject.getId());
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Integer id, @RequestBody UpdateProjectRequestDto request) {
        projectService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
