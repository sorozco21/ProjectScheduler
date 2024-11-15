package com.scheduler.project.mapper;

import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.service.TaskService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface ProjectMapper extends CommonMapper<Project, ProjectDto> {
}
