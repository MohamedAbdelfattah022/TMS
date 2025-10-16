package com.mohamed.abdelfattah.tms.repositories;

import com.mohamed.abdelfattah.tms.entities.ProjectMember;
import com.mohamed.abdelfattah.tms.entities.ProjectMemberId;
import com.mohamed.abdelfattah.tms.entities.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByIdProjectIdAndRole(Integer projectId, ProjectRole role);

    List<ProjectMember> findByIdProjectId(Integer projectId);
}