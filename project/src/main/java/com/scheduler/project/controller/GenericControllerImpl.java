package com.scheduler.project.controller;

import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.GenericService;
import com.scheduler.project.service.GenericServiceImpl;
import com.scheduler.project.service.ProjectService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class GenericControllerImpl<T, ID> implements GenericController<T,ID>{

    protected final GenericServiceImpl<T, ID> service;

    @Autowired
    protected GenericControllerImpl(GenericServiceImpl<T, ID> service) {
        this.service = service;
    }


    @PostMapping
    public Response<T> create(@RequestBody T entity) {
        return Response.ok(service.save(entity));
    }

    @GetMapping("/{id}")
    public Response<T> getById(@PathVariable ID id) throws NotFoundException {
        return Response.ok(service.findById(id));
    }

    @GetMapping
    public Response<List<T>> getAll() {
        return Response.ok(service.findAll());
    }

    @PutMapping
    public Response<T> update(@RequestBody T entity) {
        return Response.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public Response<String> delete(@PathVariable ID id) throws NotFoundException {
        return Response.ok(service.deleteById(id));
    }
}
