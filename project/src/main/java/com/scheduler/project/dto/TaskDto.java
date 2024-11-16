package com.scheduler.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class TaskDto {
    private Long id;

    @Schema(example = "Task 1")
    private String name;

    @Schema(example = "null")
    private LocalDate startDate;

    @Schema(example = "null")
    private LocalDate endDate;

    @Schema(example = "5")
    private int duration;

    @Schema(example = "null")
    private Long mainTaskId;

    @Schema(example = "null")
    private Long projectId;

    @Schema(example = "[]")
    private List<Long> subTaskIds;
}
