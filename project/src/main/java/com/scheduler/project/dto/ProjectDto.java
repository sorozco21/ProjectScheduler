package com.scheduler.project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean scheduled=false;
    private List<TaskDto> tasks;
}
