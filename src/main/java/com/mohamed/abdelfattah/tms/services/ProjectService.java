package com.mohamed.abdelfattah.tms.services;

import com.mohamed.abdelfattah.tms.dto.CreateProjectRequestDto;
import com.mohamed.abdelfattah.tms.dto.PagedResponse;
import com.mohamed.abdelfattah.tms.dto.ProjectDetailsDto;
import com.mohamed.abdelfattah.tms.dto.UpdateProjectRequestDto;
import com.mohamed.abdelfattah.tms.entities.*;
import com.mohamed.abdelfattah.tms.exceptions.ResourceNotFoundException;
import com.mohamed.abdelfattah.tms.exceptions.ValidationException;
import com.mohamed.abdelfattah.tms.mappers.ProjectMapper;
import com.mohamed.abdelfattah.tms.repositories.ProjectMemberRepository;
import com.mohamed.abdelfattah.tms.repositories.ProjectRepository;
import com.mohamed.abdelfattah.tms.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Project save(CreateProjectRequestDto requestDto) {
        Project project = ProjectMapper.mapToEntity(requestDto);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public ProjectDetailsDto findById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return ProjectMapper.mapToDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectDetailsDto> findAll() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagedResponse<ProjectDetailsDto> findAllPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Project> projectPage = projectRepository.findAll(pageable);

        return buildPagedResponse(projectPage);
    }

    @Transactional(readOnly = true)
    public PagedResponse<ProjectDetailsDto> searchProjects(String keyword, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Project> projectPage = projectRepository.searchByKeyword(keyword, pageable);

        return buildPagedResponse(projectPage);
    }

    @Transactional
    public void addMemberToProject(Integer projectId, Integer userId, ProjectRole role) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(userId);

        if (projectMemberRepository.existsById(projectMemberId))
            throw new ValidationException("User is already a member of this project");

        ProjectMember projectMember = ProjectMapper.mapToEntity(projectId, userId, project, user, role);
        projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void updateMemberRole(Integer projectId, Integer userId, ProjectRole role) {
        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(userId);

        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Project member not found"));

        projectMember.setRole(role);
        projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void removeMemberFromProject(Integer projectId, Integer userId) {
        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(projectId);
        projectMemberId.setUserId(userId);

        if (!projectMemberRepository.existsById(projectMemberId))
            throw new ResourceNotFoundException("Project member not found");

        projectMemberRepository.deleteById(projectMemberId);
    }

    @Transactional
    public void update(Integer id, UpdateProjectRequestDto requestDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        ProjectMapper.mapToEntity(existingProject, requestDto);
        projectRepository.save(existingProject);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!projectRepository.existsById(id))
            throw new ResourceNotFoundException("Project", "id", id);

        projectRepository.deleteById(id);
    }

    private PagedResponse<ProjectDetailsDto> buildPagedResponse(Page<Project> projectPage) {
        List<ProjectDetailsDto> projectDtos = projectPage.getContent().stream()
                .map(ProjectMapper::mapToDto)
                .toList();

        return PagedResponse.<ProjectDetailsDto>builder()
                .content(projectDtos)
                .pageNumber(projectPage.getNumber())
                .pageSize(projectPage.getSize())
                .totalElements(projectPage.getTotalElements())
                .totalPages(projectPage.getTotalPages())
                .last(projectPage.isLast())
                .first(projectPage.isFirst())
                .build();
    }
}