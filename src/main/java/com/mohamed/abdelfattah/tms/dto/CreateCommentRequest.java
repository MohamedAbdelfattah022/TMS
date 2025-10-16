package com.mohamed.abdelfattah.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new comment")
public class CreateCommentRequest {
    
    @NotBlank(message = "Content is required")
    @Schema(description = "Content of the comment", example = "I have started working on this task", required = true)
    private String content;
    
    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user creating the comment", example = "1", required = true)
    private Integer userId;
}