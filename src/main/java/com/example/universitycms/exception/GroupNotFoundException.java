package com.example.universitycms.exception;

public class GroupNotFoundException extends RuntimeException{

    public GroupNotFoundException() {
    }

    public GroupNotFoundException(String message) {
        super(message);
    }
}
