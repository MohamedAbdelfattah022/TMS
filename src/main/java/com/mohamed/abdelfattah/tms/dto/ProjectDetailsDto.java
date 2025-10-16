package com.mohamed.abdelfattah.tms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectDetailsDto {
    private Integer id;
    private String projectName;
    private String projectDescription;
    private List<ProjectMemberDto> projectMembers;
    private List<TaskDto> projectTasks;
}