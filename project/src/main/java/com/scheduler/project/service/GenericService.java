package com.scheduler.project.service;

import com.scheduler.project.exception.NotFoundException;

import java.util.List;

public interface GenericService<T, DTO, ID> {

    List<T> findAll();

    T findById(ID id) throws NotFoundException;

    T save(T entity);

    String deleteById(ID id) throws NotFoundException;

    DTO toDto(T entity);
    T toEntity(DTO dto);
    List<DTO> toDtoList(List<T> entityList);
    List<T> toEntityList(List<DTO> dtoList);
}