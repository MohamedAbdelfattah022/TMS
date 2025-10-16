package com.mohamed.abdelfattah.tms.dto;

import com.mohamed.abdelfattah.tms.entities.ProjectRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for adding a member to a project")
public class AddMemberRequest {
    
    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user to add to the project", example = "1", required = true)
    private Integer userId;
    
    @Schema(description = "Role of the member in the project", example = "DEVELOPER", defaultValue = "DEVELOPER")
    private ProjectRole role = ProjectRole.DEVELOPER;
}

