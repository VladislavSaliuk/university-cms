package com.example.myschedule.exception;

public class UserAlreadyAssignedException extends RuntimeException{

    public UserAlreadyAssignedException() {

    }

    public UserAlreadyAssignedException(String message) {
        super(message);
    }
}
