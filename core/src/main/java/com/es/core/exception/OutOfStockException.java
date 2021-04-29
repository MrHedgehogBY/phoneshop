package com.es.core.exception;

public class OutOfStockException extends Exception {

    public OutOfStockException(String errorMessage) {
        super(errorMessage);
    }
}
