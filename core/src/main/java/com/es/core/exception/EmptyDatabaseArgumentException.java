package com.es.core.exception;

public class EmptyDatabaseArgumentException extends Exception{

    public EmptyDatabaseArgumentException(String errorMessage) {
        super(errorMessage);
    }
}
