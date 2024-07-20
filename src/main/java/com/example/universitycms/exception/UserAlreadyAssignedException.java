package com.example.universitycms.exception;

public class UserAlreadyAssignedException extends RuntimeException{

    public UserAlreadyAssignedException() {

    }

    public UserAlreadyAssignedException(String message) {
        super(message);
    }
}
