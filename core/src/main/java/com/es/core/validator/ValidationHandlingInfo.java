package com.es.core.validator;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationHandlingInfo implements HandlingInfo {

    private List<ObjectError> errors;

    public ValidationHandlingInfo(List<ObjectError> errors) {
        this.errors = errors;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
