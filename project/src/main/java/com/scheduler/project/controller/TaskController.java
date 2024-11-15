package com.scheduler.project.controller;

import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Task;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks Controller", description = "Contains all APIs for managing tasks")
public class TaskController extends GenericControllerImpl<Task,TaskDto, Long> {

    protected TaskController(TaskService service) {
        super(service);
    }

    @PostMapping("/addSubTasks/{id}")
    public TaskDto addSubTask(@PathVariable Long id, @RequestBody List<TaskDto> dtos) throws ProjectSchedulingException {
        return ((TaskService) service).addSubTasks(id, dtos);
    }

    @DeleteMapping("/removeSubTasks/{id}")
    public TaskDto removeSubtasks(@PathVariable Long id, @RequestBody List<Long> subTaskIds) throws ProjectSchedulingException {
        return ((TaskService) service).removeSubTasks(id, subTaskIds);
    }

}
