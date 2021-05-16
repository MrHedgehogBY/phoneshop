package com.es.core.exception;

public class NoElementWithSuchIdException extends RuntimeException {

    private Long id;

    public NoElementWithSuchIdException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
