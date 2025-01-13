package com.example.myschedule.exception;

public class UserNotAssignedException extends RuntimeException {

    public UserNotAssignedException() {

    }

    public UserNotAssignedException(String message) {
        super(message);
    }
}
