package com.example.universitycms.exception;

public class UserNotAssignedException extends RuntimeException {

    public UserNotAssignedException() {

    }

    public UserNotAssignedException(String message) {
        super(message);
    }
}
