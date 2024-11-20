package com.scheduler.project.service;

import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.mapper.TaskMapper;
import com.scheduler.project.repository.ProjectRepository;
import com.scheduler.project.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TaskService extends GenericServiceImpl<Task, TaskDto, Long>{

    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository repository, TaskMapper mapper, ProjectRepository projectRepository) {
        super(repository, mapper);
        this.projectRepository = projectRepository;
    }

    public List<Task> findAllByIds(List<Long> ids){
        return repository.findAllById(ids);
    }

    public TaskDto save(TaskDto dto)  {
        try {
            Task task = toEntity(dto);
            if (dto.getProjectId() != null) {
                Project project = projectRepository.findById(dto.getProjectId())
                        .orElseThrow(() -> new ProjectSchedulingException("Project not found"));
                task.setProject(project);
            } else {
                throw new NotFoundException("Project ID must not be null");
            }

            if (dto.getMainTaskId() != null) {
                task.setMainTask(findById(dto.getMainTaskId()));
            }

            Task savedTask = save(task);
            if(dto.getSubTaskIds().isEmpty()){
                return toDto(savedTask);
            }else return addSubTasksByIds(savedTask.getId(), dto.getSubTaskIds());
        } catch (ProjectSchedulingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public TaskDto addSubTasks(Long mainTaskId, List<TaskDto> subTasks) throws ProjectSchedulingException {
        Task mainTask = findById(mainTaskId);

        if (subTasks == null || subTasks.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }

        List<Task> tasksToAdd = subTasks.stream()
//                .filter(taskDto -> taskDto.getId() != null && !mainTask.hasSubTaskWithId(taskDto.getId())) // Exclude already-added tasks
                .map(this::toEntity)
                .peek(task -> {
                    task.setMainTask(mainTask);
                    task.setProject(mainTask.getProject());
                    save(task);
                })
                .toList();

        mainTask.getSubTasks().addAll(tasksToAdd);
        repository.save(mainTask);
        return mapper.toDto(mainTask);
    }


    @Transactional
    public TaskDto removeSubTasks(Long mainTaskId, List<Long> subTaskIds) throws ProjectSchedulingException {
        Task mainTask = findById(mainTaskId);

        if (subTaskIds == null || subTaskIds.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }
        List<Task> tasksToRemove = mainTask.getSubTasks().stream()
                .filter(task -> subTaskIds.contains(task.getId()))
                .toList();

        tasksToRemove.forEach(task -> {
            task.setMainTask(null);
            repository.save(task);
        });

        return toDto(mainTask);
    }

    @Transactional
    public TaskDto addSubTasksByIds(Long mainTaskId, List<Long> subTaskIds) throws ProjectSchedulingException  {
        Task mainTask = findById(mainTaskId);

        if (subTaskIds == null || subTaskIds.isEmpty()) {
            throw new ProjectSchedulingException("SubTasks cannot be null or empty");
        }

        List<Task> tasksToAdd = subTaskIds.stream()
                .map(subTaskId -> {
                    try {
                        return findById(subTaskId);
                    } catch (NotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(task -> task.getMainTask() == null || !task.getMainTask().equals(mainTask)) // Exclude already assigned subtasks
                .peek(task -> {
                    task.setMainTask(mainTask);
                    task.setProject(mainTask.getProject());
                    repository.save(task);
                })
                .toList();

        mainTask.getSubTasks().addAll(tasksToAdd);
        repository.save(mainTask);

        return toDto(mainTask);
    }

}
