package com.mohamed.abdelfattah.tms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequestDto {
    @NotBlank(message = "Project name is required")
    private String projectName;

    private String projectDescription;
}