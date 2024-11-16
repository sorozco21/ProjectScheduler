package com.scheduler.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectDto {
    private Long id;

    @Schema(example = "Project 1")
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    @Schema(example = "false")
    private boolean scheduled=false;

    private List<TaskDto> tasks;
}
