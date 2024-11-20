package com.scheduler.project.mapper;

import com.scheduler.project.dto.CreateTaskRequest;
import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TaskMapper extends CommonMapper<Task, TaskDto> {

    @Mapping(target = "subTaskIds", source = "subTasks", qualifiedByName = "subTasksToIds")
    @Mapping(target = "mainTaskId", source = "mainTask", qualifiedByName = "mainTaskToId")
    @Mapping(target = "projectId", source = "project", qualifiedByName = "projectToId")
    TaskDto toDto(Task entity);

    @Mapping(target = "project", source = "projectId", qualifiedByName = "projectIdToEntity")
    @Mapping(target = "subTasks", source = "subTaskIds", qualifiedByName = "subTaskIdsToEntityList")
    @Mapping(target = "mainTask", source = "mainTaskId", qualifiedByName = "mainTaskIdToEntity")
    Task toEntity(TaskDto dto);

    Task toEntity(CreateTaskRequest req);
    List<Task> requestToEntityList(List<CreateTaskRequest> list);

    @Named("projectIdToEntity")
    default Project projectIdToEntity(Long id) {
        if(id != null){
            return new Project(id);
        } return null;
    }

    @Named("mainTaskIdToEntity")
    default Task mainTaskIdToEntity(Long id) {
        if(id != null){
            return new Task(id);
        } return null;
    }

    @Named("subTaskIdsToEntityList")
    default Set<Task> subTaskIdsToEntityList(List<Long> ids) {
        return (ids != null && !ids.isEmpty())
                ? ids.stream()
                .map(Task::new).collect(Collectors.toSet())
                : null;
    }

    @Named("subTasksToIds")
    default List<Long> subTasksToIds(Set<Task> subTasks) {
        if (subTasks == null || subTasks.isEmpty()) {
            return new ArrayList<>();
        }
        return subTasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());
    }

    @Named("mainTaskToId")
    default Long mainTaskToId(Task mainTask) {
        if(mainTask != null && mainTask.getId()!=null){
            return mainTask.getId();
        }return null;
    }
    @Named("projectToId")
    default Long projectToId(Project project) {
        if(project != null && project.getId()!=null){
            return project.getId();
        }return null;
    }
}