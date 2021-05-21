package com.es.core.exception;

public class NoElementWithSuchIdException extends RuntimeException {

    private String id;

    public NoElementWithSuchIdException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
