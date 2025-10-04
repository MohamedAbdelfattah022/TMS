package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.ProjectDetailsDto;
import com.mohamed.abdelfattah.tms.entities.Project;
import com.mohamed.abdelfattah.tms.entities.ProjectMember;
import com.mohamed.abdelfattah.tms.entities.ProjectMemberId;
import com.mohamed.abdelfattah.tms.entities.User;
import com.mohamed.abdelfattah.tms.mappers.ProjectMapper;
import com.mohamed.abdelfattah.tms.repositories.ProjectMemberRepository;
import com.mohamed.abdelfattah.tms.repositories.ProjectRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public ProjectDetailsDto findById(Integer id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return ProjectMapper.mapToDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDetailsDto> findAll() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMemberToProject(Integer projectId, Integer userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(userId);

        ProjectMember projectMember = new ProjectMember();
        projectMember.setId(projectMemberId);
        projectMember.setProject(project);
        projectMember.setUser(user);

        projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void removeMemberFromProject(Integer projectId, Integer userId) {
        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(userId);

        projectMemberRepository.deleteById(projectMemberId);
    }

    public void update(Integer id, Project project) {
        Project existingProject = projectRepository.findById(id).orElseThrow();
        existingProject.setDescription(project.getDescription());
        existingProject.setProjectName(project.getProjectName());
        projectRepository.save(existingProject);
    }

    public void deleteById(Integer id) {
        projectRepository.deleteById(id);
    }
}