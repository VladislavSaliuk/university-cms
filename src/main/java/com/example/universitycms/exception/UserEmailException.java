package com.example.universitycms.exception;

public class UserEmailException extends RuntimeException {

    public UserEmailException() {

    }
    public UserEmailException(String message) {
        super(message);
    }
}
