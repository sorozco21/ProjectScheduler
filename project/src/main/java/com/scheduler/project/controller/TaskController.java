package com.scheduler.project.controller;

import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Task;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.ProjectService;
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

    @Override
    @PostMapping
    public Response<TaskDto> create(@RequestBody TaskDto dto)  {
        return Response.ok(((TaskService)service).save(dto));
    }

    @Override
    @PutMapping
    public Response<TaskDto> update(@RequestBody TaskDto dto) {
        return create(dto);
    }

    @PostMapping("/{id}/addSubTasks")
    public TaskDto addSubTask(@PathVariable Long id, @RequestBody List<TaskDto> dtos) throws ProjectSchedulingException {
        return ((TaskService) service).addSubTasks(id, dtos);
    }
    @PostMapping("/{id}/addSubTasksByIds")
    public TaskDto addSubTaskByIds(@PathVariable Long id, @RequestBody List<Long> ids) throws ProjectSchedulingException {
        return ((TaskService) service).addSubTasksByIds(id, ids);
    }

    @DeleteMapping("/{id}/removeSubTasksByIds")
    public TaskDto removeSubtasks(@PathVariable Long id, @RequestBody List<Long> subTaskIds) throws ProjectSchedulingException {
        return ((TaskService) service).removeSubTasks(id, subTaskIds);
    }

}
