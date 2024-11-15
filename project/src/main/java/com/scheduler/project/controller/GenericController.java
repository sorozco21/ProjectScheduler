package com.scheduler.project.controller;

import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.other.Response;

import java.util.List;

public interface GenericController<DTO, ID> {
    Response<DTO> create(DTO dto);

    Response<DTO> getById(ID id) throws NotFoundException ;

    Response<List<DTO>> getAll() ;

    Response<DTO> update(DTO dto);

    Response<String> delete(ID id) throws NotFoundException ;


}
