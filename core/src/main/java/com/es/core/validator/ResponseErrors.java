package com.es.core.validator;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseErrors {

    private String errorsMessage;

    public ResponseErrors() {

    }

    public ResponseErrors(List<ObjectError> errors) {
        errorsMessage = errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
    }

    public String getErrorsMessage() {
        return errorsMessage;
    }

    public void setErrorsMessage(String errorsMessage) {
        this.errorsMessage = errorsMessage;
    }
}
