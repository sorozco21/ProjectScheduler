package com.scheduler.project.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {
    private String name;
    @Positive
    private int duration;
}
