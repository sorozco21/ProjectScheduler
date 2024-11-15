package com.scheduler.project.service;

import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import com.scheduler.project.exception.CyclicDependencyException;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.mapper.ProjectMapper;
import com.scheduler.project.repository.ProjectRepository;
import com.scheduler.project.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class ProjectService extends GenericServiceImpl<Project, ProjectDto, Long>{
    private final ProjectRepository projectRepository;

    private final TaskService taskService;

    @Autowired
    public ProjectService(ProjectRepository repository, ProjectMapper mapper, TaskService taskService,
                          ProjectRepository projectRepository) {
        super(repository, mapper);
        this.taskService = taskService;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public ProjectDto addTasks(Long projectId, List<TaskDto> tasks) throws ProjectSchedulingException {
        Project project = findById(projectId);
        log.info("this is the project to add task : {}", project.getId());
        if (tasks == null || tasks.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }

        List<Task> tasksToAdd = tasks.stream()
                //.filter(taskDto -> taskDto.getId() != null && !project.hasTaskWithId(taskDto.getId())) // Exclude already-added tasks
                .map(taskService::toEntity)
                .peek(task -> {
                    task.setProject(project);
                    taskService.save(task);
                })
                .toList();
        project.getTasks().addAll(tasksToAdd);
        repository.save(project);
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto addTaskByIds(Long projectId, List<Long> taskIds) throws ProjectSchedulingException {
        Project project = findById(projectId);
        if (taskIds == null || taskIds.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }
        List<Task> tasksToAdd = taskService.findAllByIds(taskIds).stream()
                .filter(task -> !project.getTasks().contains(task))
                .peek(task -> {
                    task.setProject(project);
                    taskService.save(task);
                })
                .toList();

        project.getTasks().addAll(tasksToAdd);
        repository.save(project);
        return mapper.toDto(project);
    }

    @Transactional
    public ProjectDto removeTaskIds(Long projectId, List<Long> taskIds) throws ProjectSchedulingException {
        Project project = findById(projectId);

        if (taskIds == null || taskIds.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }
        List<Task> tasksToRemove = project.getTasks().stream()
                .filter(task -> taskIds.contains(task.getId()))
                .peek(task -> {
                    task.setProject(null);
                    taskService.save(task);
                })
                .toList();

        project.getTasks().removeAll(tasksToRemove);
        repository.save(project);
        return mapper.toDto(project);
    }

    public ProjectDto schedule(Long id) throws CyclicDependencyException, NotFoundException {
        Project project = findById(id);
        List<Task> sortedTasks = topologicalSort(project);
        List<Task> sortedAndScheduledTask = new ArrayList<>();
        LocalDate currDate = project.getStartDate();
        for(Task task : sortedTasks){
            if(task.getSubTasks().isEmpty()){
                task.setStartAndEndDate(currDate);
            }else{
                LocalDate latestEndDate = project.getStartDate();
                for(Task subTask : task.getSubTasks()){
                    if(subTask.getEndDate() != null && subTask.getEndDate().isAfter(latestEndDate)){
                        latestEndDate = subTask.getEndDate();
                    }
                }
                task.setStartAndEndDate(
                        currDate.isAfter(latestEndDate) ? currDate
                                : latestEndDate.plusDays(1)
                );
            }
            currDate = task.getEndDate().plusDays(1);
            sortedAndScheduledTask.add(task);
        }
        project.getTasks().clear();
        project.getTasks().addAll(sortedAndScheduledTask);
        project.setEndDate(currDate);
        project.setScheduled(true);
        projectRepository.save(project);

        return mapper.toDto(project);
    }

    private List<Task> topologicalSort(Project project) throws CyclicDependencyException {
        List<Task> sortedTasks = new ArrayList<>();
        Queue<Task> taskQueue = new LinkedList<>();
        Map<Task, Integer> inDegreeMap = new HashMap<>();
        for(Task task : project.getTasks()){
            inDegreeMap.put(task, task.getSubTasks().size());
            if(inDegreeMap.get(task) == 0){
                taskQueue.add(task);
            }
        }

        while(!taskQueue.isEmpty()){
            Task currentTask = taskQueue.poll();
            sortedTasks.add(currentTask);

            for (Task task : project.getTasks()){
                if(task.getSubTasks().contains(currentTask)){
                    int updatedInDegree = inDegreeMap.get(task) -1;
                    inDegreeMap.put(task, updatedInDegree);

                    if(updatedInDegree == 0){
                        taskQueue.add(task);
                    }
                }
            }
        }

        if(sortedTasks.size() != project.getTasks().size()){
            throw new CyclicDependencyException(("Cycle detected in tasks."));
        }

        return sortedTasks;
    }
}
