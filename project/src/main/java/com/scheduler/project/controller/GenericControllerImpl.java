package com.scheduler.project.controller;

import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.other.Response;
import com.scheduler.project.service.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class GenericControllerImpl<T,DTO, ID>
        implements GenericController<DTO, ID> {

    protected final GenericServiceImpl<T,DTO, ID> service;

    @Autowired
    protected GenericControllerImpl(GenericServiceImpl<T, DTO, ID> service) {
        this.service = service;
    }


    @PostMapping
    public Response<DTO> create(@RequestBody DTO dto) {
        T entity =  service.toEntity(dto);
        return Response.ok(service.toDto(service.save(entity)));
    }

    @GetMapping("/{id}")
    public Response<DTO> getById(@PathVariable ID id) throws NotFoundException {
        return Response.ok(service.toDto(service.findById(id)));
    }

    @GetMapping
    public Response<List<DTO>> getAll() {
        return Response.ok(service.toDtoList(service.findAll()));
    }

    @PutMapping
    public Response<DTO> update(@RequestBody DTO dto) {
        T entity =  service.toEntity(dto);
        return Response.ok(service.toDto(service.save(entity)));
    }

    @DeleteMapping("/{id}")
    public Response<String> delete(@PathVariable ID id) throws NotFoundException {
        return Response.ok(service.deleteById(id));
    }
}
