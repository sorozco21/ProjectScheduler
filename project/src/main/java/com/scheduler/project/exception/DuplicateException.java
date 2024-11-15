package com.scheduler.project.exception;

import java.sql.SQLIntegrityConstraintViolationException;

public class DuplicateException extends SQLIntegrityConstraintViolationException {
    public DuplicateException(String message) {
        super(message);
    }
}
