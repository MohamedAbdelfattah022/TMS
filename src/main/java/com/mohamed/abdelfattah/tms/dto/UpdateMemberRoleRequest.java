package com.mohamed.abdelfattah.tms.dto;

import com.mohamed.abdelfattah.tms.entities.ProjectRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberRoleRequest {
    @NotNull(message = "Role is required")
    private ProjectRole role;
}

