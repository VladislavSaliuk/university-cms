package com.example.myschedule.exception;

public class UserEmailException extends RuntimeException {

    public UserEmailException() {

    }
    public UserEmailException(String message) {
        super(message);
    }
}
