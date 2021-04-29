package com.es.core.validator;

public class ResponseErrors implements Errors {

    private String errorMessage;

    public ResponseErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorsMessage() {
        return errorMessage;
    }

    public void setErrorsMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
