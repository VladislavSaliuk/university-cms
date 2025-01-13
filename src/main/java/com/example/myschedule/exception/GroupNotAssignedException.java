package com.example.myschedule.exception;

public class GroupNotAssignedException extends RuntimeException {

    public GroupNotAssignedException() {

    }

    public GroupNotAssignedException(String message) {
        super(message);
    }
}
