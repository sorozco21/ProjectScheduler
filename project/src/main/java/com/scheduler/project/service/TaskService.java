package com.scheduler.project.service;

import com.scheduler.project.entity.Task;
import com.scheduler.project.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService extends GenericServiceImpl<Task, Long>{
    public TaskService(TaskRepository repository) {
        super(repository);
    }
}
