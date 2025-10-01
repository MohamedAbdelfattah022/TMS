package com.mohamed.abdelfattah.tms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectMemberDto {
    private Integer userId;
    private String fullName;
    private String email;
}