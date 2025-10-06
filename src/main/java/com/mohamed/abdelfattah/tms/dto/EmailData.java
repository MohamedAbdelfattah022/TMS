package com.mohamed.abdelfattah.tms.dto;

public record EmailData(
        String to,
        String userName,
        String taskTitle,
        String taskPriority,
        String taskDescription
) {

}
