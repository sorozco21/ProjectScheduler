package com.scheduler.project.controller;

import com.scheduler.project.dto.CreateProjectRequest;
import com.scheduler.project.dto.CreateTaskRequest;
import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.exception.CyclicDependencyException;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
@Tag(name = "Projects Controller", description = "Contains all APIs for managing projects")
public class ProjectController {

    private final ProjectService service;

    @PostMapping
    public Response<ProjectDto> create(@RequestBody CreateProjectRequest request) {
        return Response.ok(service.save(request));
    }

    @PatchMapping("/{id}/calculate-schedule")
    public Response<ProjectDto> calculateSchedule(@PathVariable Long id) throws NotFoundException, CyclicDependencyException {
        return Response.ok(service.schedule(id));
    }

    @PostMapping("{id}/tasks")
    public Response<ProjectDto> addTasks(@PathVariable Long id, @RequestBody List<CreateTaskRequest> requests) throws NotFoundException {
        return Response.ok(service.addTasks(id, requests));
    }

    @PostMapping("{id}/tasks/{mainTaskId}")
    public Response<ProjectDto> addSubTasks(@PathVariable Long id, @PathVariable Long mainTaskId, @RequestBody Set<CreateTaskRequest> subTasksRequest) throws NotFoundException {
        return Response.ok(service.addSubTasks(id, mainTaskId, subTasksRequest));
    }

    @DeleteMapping("/tasks/{id}")
    public Response<String> deleteTasksByIds(@PathVariable Long id) throws NotFoundException {
        return Response.ok(service.deleteTaskById(id));
    }

    @DeleteMapping("{id}")
    public Response<String> deleteProjectById(@PathVariable Long id) throws NotFoundException {
        return Response.ok(service.deleteById(id));
    }

    @GetMapping("{id}")
    public Response<ProjectDto> getById(@PathVariable Long id) throws NotFoundException {
        return Response.ok(service.toDto(service.findById(id)));
    }
}
