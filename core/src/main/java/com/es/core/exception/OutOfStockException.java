package com.es.core.exception;

public class OutOfStockException extends CustomException {

    public OutOfStockException() {

    }

    public OutOfStockException(String errorMessage) {
        super(errorMessage);
    }
}
