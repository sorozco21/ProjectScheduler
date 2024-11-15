package com.scheduler.project.service;

import com.scheduler.project.entity.Project;
import com.scheduler.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends GenericServiceImpl<Project, Long>{
    public ProjectService(ProjectRepository repository) {
        super(repository);
    }
}
