package com.scheduler.project.mapper;

import com.scheduler.project.dto.TaskDto;
import com.scheduler.project.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends CommonMapper<Task, TaskDto> {

}