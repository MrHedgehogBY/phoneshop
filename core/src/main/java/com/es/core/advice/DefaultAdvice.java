package com.es.core.advice;

import com.es.core.exception.EmptyDatabaseArgumentException;
import com.es.core.exception.OutOfStockException;
import com.es.core.validator.ResponseErrors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<ResponseErrors> handleOutOfStockException(OutOfStockException e) {
        ResponseErrors errors = new ResponseErrors();
        errors.setErrorsMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmptyDatabaseArgumentException.class)
    public ResponseEntity<ResponseErrors> handleEmptyDatabaseArgumentException(EmptyDatabaseArgumentException e) {
        ResponseErrors errors = new ResponseErrors();
        errors.setErrorsMessage(e.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
