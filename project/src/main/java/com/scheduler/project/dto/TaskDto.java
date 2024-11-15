package com.scheduler.project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int duration;
    private Long mainTaskId;
    private Long projectId;
    private Set<Long> subTaskIds;
}
