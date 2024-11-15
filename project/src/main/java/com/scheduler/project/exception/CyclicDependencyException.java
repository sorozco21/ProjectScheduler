package com.scheduler.project.exception;

public class CyclicDependencyException extends ProjectSchedulingException{
    public CyclicDependencyException(String message) {
        super(message);
    }
}
