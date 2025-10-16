package com.mohamed.abdelfattah.tms.dto;

import com.mohamed.abdelfattah.tms.entities.Category;
import com.mohamed.abdelfattah.tms.entities.Priority;
import com.mohamed.abdelfattah.tms.entities.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Task Data Transfer Object")
public class TaskDto {

    @Schema(description = "Task ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "Title is required")
    @Schema(description = "Task title", example = "Implement user authentication", required = true)
    private String title;

    @Schema(description = "Task description", example = "Add JWT-based authentication to the application")
    private String description;

    @NotNull(message = "Status is required")
    @Schema(description = "Task status", example = "TODO", required = true)
    private Status status;

    @NotNull(message = "Priority is required")
    @Schema(description = "Task priority", example = "HIGH", required = true)
    private Priority priority;

    @NotNull(message = "Category is required")
    @Schema(description = "Task category", example = "FEATURE", required = true)
    private Category category;

    @Schema(description = "Attachment URL", example = "https://example.com/file.pdf")
    private String attachmentUrl;

    @NotNull(message = "Assignee ID is required")
    @Schema(description = "ID of the user assigned to this task", example = "1", required = true)
    private Integer assigneeId;

    @Schema(description = "ID of the project this task belongs to", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer projectId;
}