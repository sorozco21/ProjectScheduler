package com.scheduler.project.controller;

import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.other.Response;

import java.util.List;

public interface GenericController<T, ID> {
    Response<T> create(T entity);

    Response<T> getById(ID id) throws NotFoundException ;

    Response<List<T>> getAll() ;

    Response<T> update(T entity);

    Response<String> delete(ID id) throws NotFoundException ;


}
