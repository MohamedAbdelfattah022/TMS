package com.mohamed.abdelfattah.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new project")
public class CreateProjectRequestDto {
    
    @NotBlank(message = "Project name is required")
    @Schema(description = "Name of the project", example = "Task Management System", required = true)
    private String projectName;
    
    @Schema(description = "Detailed description of the project", example = "A comprehensive task management application")
    private String projectDescription;
}