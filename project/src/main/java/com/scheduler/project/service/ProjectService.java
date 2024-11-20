package com.scheduler.project.service;

import com.scheduler.project.constant.MessageConstants;
import com.scheduler.project.dto.CreateProjectRequest;
import com.scheduler.project.dto.CreateTaskRequest;
import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import com.scheduler.project.exception.CyclicDependencyException;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.mapper.ProjectMapper;
import com.scheduler.project.mapper.TaskMapper;
import com.scheduler.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService extends GenericServiceImpl<Project, ProjectDto, Long>{
    private final ProjectRepository projectRepository;

    private final TaskService taskService;

    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;

    @Autowired
    public ProjectService(ProjectRepository repository, TaskService taskService,
                          ProjectRepository projectRepository, ProjectMapper projectMapper, TaskMapper taskMapper) {
        super(repository, projectMapper);
        this.taskService = taskService;
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
    }

    public ProjectDto save(CreateProjectRequest request){
        Project project = projectMapper.toEntity(request);
        project.getTasks().clear();
        List<Task> tasks = request.getTasks().stream()
                .map(taskMapper::toEntity)
                .toList();
        tasks.forEach(task -> task.setProject(project));
        project.getTasks().addAll(tasks);
        return toDto(save(project));
    }

    public ProjectDto addTasks(Long id, List<CreateTaskRequest> requests) throws NotFoundException {
        Project project = findById(id);
        List<Task> tasks = taskMapper.requestToEntityList(requests);
        tasks.forEach(task -> task.setProject(project));
        project.getTasks().addAll(tasks);
        project.setScheduled(false);
        return toDto(save(project));
    }

    public ProjectDto addSubTasks(Long projectId, Long mainTaskId, Set<CreateTaskRequest> subTasksRequest) throws NotFoundException {
        Project project = findById(projectId);
        Task mainTask = taskService.findById(mainTaskId);
        Set<Task> subTasks = subTasksRequest.stream()
                .map(taskMapper::toEntity)
                .collect(Collectors.toSet());

        subTasks.forEach(subTask -> {
            subTask.setProject(project);
            subTask.setMainTask(mainTask);
        });
        mainTask.getSubTasks().addAll(subTasks);
        project.getTasks().addAll(subTasks);
        project.setScheduled(false);
        return toDto(save(project));
    }

    public String deleteTaskById(Long id) throws NotFoundException {
        return taskService.deleteById(id);
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
            throw new CyclicDependencyException(MessageConstants.CYCLIC_DEPENDENCY_EXCEPTION);
        }

        return sortedTasks;
    }
}
