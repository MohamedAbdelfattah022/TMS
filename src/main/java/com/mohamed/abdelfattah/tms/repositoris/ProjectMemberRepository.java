package com.mohamed.abdelfattah.tms.repositoris;

import com.mohamed.abdelfattah.tms.entities.ProjectMember;
import com.mohamed.abdelfattah.tms.entities.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
}