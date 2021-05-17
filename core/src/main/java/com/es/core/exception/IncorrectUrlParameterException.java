package com.es.core.exception;

public class IncorrectUrlParameterException extends RuntimeException {

    private String parameter;

    public IncorrectUrlParameterException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
