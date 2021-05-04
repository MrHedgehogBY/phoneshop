package com.es.core.exception;

public class NoElementWithSuchIdException extends CustomException {


    public NoElementWithSuchIdException() {
        String errorMessage = "No element with such id";
        this.setErrorMessage(errorMessage);
    }
}
