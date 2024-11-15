package com.scheduler.project.controller;

import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.GenericServiceImpl;
import com.scheduler.project.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController extends GenericControllerImpl<Task, Long> {

    protected TaskController(TaskService service) {
        super(service);
    }


}
