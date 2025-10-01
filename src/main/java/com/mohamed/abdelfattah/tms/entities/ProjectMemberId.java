package com.mohamed.abdelfattah.tms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProjectMemberId implements Serializable {
    private static final long serialVersionUID = 8872298687391336303L;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProjectMemberId entity = (ProjectMemberId) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.projectId, entity.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, projectId);
    }

}