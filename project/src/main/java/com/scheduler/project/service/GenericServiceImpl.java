package com.scheduler.project.service;

import com.scheduler.project.constant.MessageConstants;
import com.scheduler.project.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j
public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    @Autowired
    public GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(ID id) throws NotFoundException {
        return repository.findById(id).orElseThrow(()-> new NotFoundException(String.format(MessageConstants.NOT_FOUND_EXCEPTION, "ID",id)));
    }

    @Override
    public T save(T entity) {
        log.info("SAVING ENTITY: {}", entity);
        return repository.save(entity);
    }

    @Override
    public String deleteById(ID id) throws NotFoundException {
        if(repository.existsById(id)) {
            repository.deleteById(id);
            return "Deletion Success";
        }
        else{
            throw new NotFoundException(String.format(MessageConstants.NOT_FOUND_EXCEPTION, "ID",id));
        }
    }
}
