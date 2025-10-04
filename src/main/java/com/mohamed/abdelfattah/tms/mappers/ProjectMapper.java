package com.mohamed.abdelfattah.tms.mappers;

import com.mohamed.abdelfattah.tms.dto.CreateProjectRequestDto;
import com.mohamed.abdelfattah.tms.dto.ProjectDetailsDto;
import com.mohamed.abdelfattah.tms.dto.ProjectMemberDto;
import com.mohamed.abdelfattah.tms.dto.TaskDto;
import com.mohamed.abdelfattah.tms.entities.Project;
import com.mohamed.abdelfattah.tms.entities.ProjectMember;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ProjectMapper {
    public static ProjectDetailsDto mapToDto(Project project) {
        List<ProjectMemberDto> projectMemberDtos = Optional.ofNullable(project.getProjectMembers())
                .orElse(Collections.emptyList())
                .stream()
                .map(ProjectMapper::mapToDto)
                .collect(Collectors.toList());

        List<TaskDto> taskDtos = Optional.ofNullable(project.getTasks())
                .orElse(Collections.emptyList())
                .stream()
                .map(TaskMapper::mapToDto)
                .collect(Collectors.toList());

        return ProjectDetailsDto.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .projectDescription(project.getDescription())
                .projectMembers(projectMemberDtos)
                .projectTasks(taskDtos)
                .build();
    }

    public static ProjectMemberDto mapToDto(ProjectMember projectMember) {
        return ProjectMemberDto.builder()
                .userId(projectMember.getId().getUserId())
                .fullName(projectMember.getUser().getFullName())
                .email(projectMember.getUser().getEmail())
                .build();
    }

    public static Project mapToEntity(CreateProjectRequestDto requestDto) {
        return Project.builder()
                .projectName(requestDto.getProjectName())
                .description(requestDto.getProjectDescription())
                .build();
    }
}
