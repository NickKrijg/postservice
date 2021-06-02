package com.kwetter.postservice.exception;

public class InvalidPostRefenerceExpection extends RuntimeException {
    public InvalidPostRefenerceExpection(String errorMessage) {
        super(errorMessage);
    }
}
