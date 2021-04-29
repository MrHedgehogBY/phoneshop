package com.es.core.validator;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationErrors implements Errors {

    private List<ObjectError> errors;

    public ValidationErrors(List<ObjectError> errors) {
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
