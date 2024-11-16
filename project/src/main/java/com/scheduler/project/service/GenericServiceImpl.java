package com.scheduler.project.service;

import com.scheduler.project.constant.MessageConstants;
import com.scheduler.project.exception.NotFoundException;
import com.scheduler.project.mapper.CommonMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Slf4j
@Getter
public abstract class GenericServiceImpl<T, DTO, ID>
        implements GenericService<T, DTO, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final CommonMapper<T, DTO> mapper;

    @Autowired
    public GenericServiceImpl(JpaRepository<T, ID> repository, CommonMapper<T, DTO> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(ID id) throws NotFoundException {
        return repository.findById(id)
                .orElseThrow(()-> new NotFoundException(String.format(MessageConstants.NOT_FOUND_EXCEPTION, "ID",id)));
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

    @Override
    public DTO toDto(T entity){
        return mapper.toDto(entity);
    }
    @Override
    public T toEntity(DTO dto){
        return mapper.toEntity(dto);
    }

    @Override
    public List<DTO> toDtoList(List<T> entityList){
        return mapper.toDtoList(entityList);
    }
    @Override
    public List<T> toEntityList(List<DTO> dtoList){
        return mapper.toEntityList(dtoList);
    }
}
