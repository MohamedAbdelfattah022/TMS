package com.mohamed.abdelfattah.tms.repositories;

import com.mohamed.abdelfattah.tms.entities.Category;
import com.mohamed.abdelfattah.tms.entities.Priority;
import com.mohamed.abdelfattah.tms.entities.Status;
import com.mohamed.abdelfattah.tms.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Task> searchByTitleOrDescription(@Param("keyword") String keyword, Pageable pageable);

    Page<Task> findByAssigneeId(Integer assigneeId, Pageable pageable);

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByCategory(Category category, Pageable pageable);

    Page<Task> findByProjectId(Integer projectId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:category IS NULL OR t.category = :category) AND " +
            "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
            "(:projectId IS NULL OR t.project.id = :projectId)")
    Page<Task> searchWithFilters(
            @Param("title") String title,
            @Param("description") String description,
            @Param("status") Status status,
            @Param("priority") Priority priority,
            @Param("category") Category category,
            @Param("assigneeId") Integer assigneeId,
            @Param("projectId") Integer projectId,
            Pageable pageable
    );
}
