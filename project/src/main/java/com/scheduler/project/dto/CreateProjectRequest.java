package com.scheduler.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class CreateProjectRequest {
    private String name;
    private LocalDate startDate;
    private List<CreateTaskRequest> tasks;
}
