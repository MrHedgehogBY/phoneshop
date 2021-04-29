package com.es.core.exception;

public class CustomException extends Exception {

    public String errorMessage;

    public CustomException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
