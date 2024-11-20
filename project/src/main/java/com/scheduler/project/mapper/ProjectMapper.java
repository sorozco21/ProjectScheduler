package com.scheduler.project.mapper;

import com.scheduler.project.dto.CreateProjectRequest;
import com.scheduler.project.dto.CreateTaskRequest;
import com.scheduler.project.dto.ProjectDto;
import com.scheduler.project.entity.Project;
import com.scheduler.project.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface ProjectMapper extends CommonMapper<Project, ProjectDto> {

    Project toEntity(CreateProjectRequest req);

    Task toEntity(CreateTaskRequest req);

}
