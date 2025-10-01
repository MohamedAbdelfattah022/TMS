package com.mohamed.abdelfattah.tms.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskDto {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String category;
    private String attachmentUrl;
    private Integer assigneeId;
}