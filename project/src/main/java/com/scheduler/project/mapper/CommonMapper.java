package com.scheduler.project.mapper;

import java.util.List;

public interface CommonMapper<E, D> {
    public E toEntity(D dto);

    public List<E> toEntityList(List<D> dtoList);

    public D toDto(E entity);

    public List<D> toDtoList(List<E> entityList);
}
