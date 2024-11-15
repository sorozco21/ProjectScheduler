package com.scheduler.project.controller;

import com.scheduler.project.entity.Project;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.GenericServiceImpl;
import com.scheduler.project.service.ProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("projects")
public class ProjectController extends GenericControllerImpl<Project, Long> {
    protected ProjectController(ProjectService service) {
        super(service);
    }
    @PostMapping("/test")
    public Response<Project> sample(){
        Project project = new Project();
        project.setName("SAMPLE PROJECT NAME");
        project.setStartDate(LocalDate.now());
        project.setEndDate(LocalDate.now().plusDays(2));
        return Response.ok(service.save(project));
    }
}
