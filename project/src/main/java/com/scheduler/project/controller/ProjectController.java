package com.scheduler.project.controller;

import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.exception.CyclicDependencyException;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projects")
@Tag(name = "Projects Controller", description = "Contains all APIs for managing projects")
public class ProjectController extends GenericControllerImpl<Project, ProjectDto, Long> {

    @Autowired
    protected ProjectController(ProjectService service) {
        super(service);
    }
    @PostMapping("/{id}/calculate-schedule")
    public ProjectDto calculateSchedule(@PathVariable Long id) throws NotFoundException, CyclicDependencyException {
        return ((ProjectService) service).schedule(id);
    }

    @PostMapping("{id}/addTasks")
    public ProjectDto addTasks(@PathVariable Long id, @RequestBody List<TaskDto> tasks) throws ProjectSchedulingException {
        return ((ProjectService) service).addTasks(id, tasks);
    }

    @PostMapping("{id}/addTaskByIds")
    public ProjectDto addTasksByIds(@PathVariable Long id, @RequestBody List<Long> taskIds) throws ProjectSchedulingException {
        return ((ProjectService) service).addTaskByIds(id, taskIds);
    }

    @DeleteMapping("{id}/removeTasks")
    public ProjectDto removeTasks(@PathVariable Long id, @RequestBody List<Long> taskIds) throws ProjectSchedulingException {
        return ((ProjectService) service).removeTaskIds(id, taskIds);
    }
}
