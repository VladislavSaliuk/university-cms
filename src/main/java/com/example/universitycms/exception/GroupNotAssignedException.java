package com.example.universitycms.exception;

public class GroupNotAssignedException extends RuntimeException {

    public GroupNotAssignedException() {

    }

    public GroupNotAssignedException(String message) {
        super(message);
    }
}
