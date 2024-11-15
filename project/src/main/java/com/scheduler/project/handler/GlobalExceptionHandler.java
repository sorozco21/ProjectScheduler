package com.scheduler.project.handler;

import com.scheduler.project.exception.CyclicDependencyException;
import com.scheduler.project.exception.ProjectSchedulingException;
import com.scheduler.project.other.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProjectSchedulingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response<String> handleApiErrorException(ProjectSchedulingException ex) {
        return Response.<String>builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Response<String> handleUniqueException(SQLIntegrityConstraintViolationException ex) {
        return Response.<String>builder()
                .message(ex.getMessage())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }
}
